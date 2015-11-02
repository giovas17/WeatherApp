package com.giovas.network;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;

import com.giovas.listeners.DownloadListener;
import com.giovas.objects.Forecast;
import com.giovas.objects.WeatherObject;
import com.giovas.utils.Utility;
import com.giovas.weatherapi.R;
import com.giovas.weatherapi.WeatherAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by DarkGeat on 10/31/2015.
 */
public class NetworkConnection extends AsyncTask<String,Void,Boolean> {

    private static String LOG_TAG = NetworkConnection.class.getSimpleName();
    // Will contain the raw JSON response as a string.
    private String forecastJsonStr = null;
    private String zipcode = null;
    private DownloadListener listener;
    private Context context;

    public NetworkConnection(Context context, DownloadListener listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Log.d(LOG_TAG, "onPerformUpdate called.");

        zipcode = params[0];

        if (zipcode.length() > 0) {

            String locationQuery = zipcode;

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            forecastJsonStr = "";

            String format = "json";
            String units = "metric";
            int numDays = 14;
            String localLanguageDevice = Locale.getDefault().getLanguage();

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String FORECAST_BASE_URL =
                        "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String LANGUAGE_PARAM = "lang";
                final String APIKEY = "APPID";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, locationQuery)
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(LANGUAGE_PARAM, localLanguageDevice)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .appendQueryParameter(APIKEY,context.getString(R.string.api_key))
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream != null) {
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() > 0) {
                        forecastJsonStr = buffer.toString();
                        WeatherAPI.setLocationStatus(context, WeatherAPI.LOCATION_STATUS_OK);
                        return true;
                    }else{
                        WeatherAPI.setLocationStatus(context, WeatherAPI.LOCATION_STATUS_SERVER_DOWN);
                        return false;
                    }

                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                WeatherAPI.setLocationStatus(context, WeatherAPI.LOCATION_STATUS_SERVER_DOWN);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
                return true;
            }
        }else {
            WeatherAPI.setLocationStatus(context, WeatherAPI.STATUS_NO_ZIPCODES);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result){
            try {
                listener.obtainWeatherObject(getWeatherDataFromJson(forecastJsonStr));
                Log.d(LOG_TAG,"Weather Object created successfully");
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                listener.obtainWeatherObject(new WeatherObject(null, null, zipcode));
                WeatherAPI.setLocationStatus(context, WeatherAPI.LOCATION_STATUS_SERVER_INVALID);
                e.printStackTrace();
            }
        }else {
            listener.obtainWeatherObject(new WeatherObject(null,null,zipcode));
            Log.e(LOG_TAG,"Weather Object won't be created");
        }
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private WeatherObject getWeatherDataFromJson(String forecastJsonStr)
            throws JSONException {

        WeatherObject listWeather = new WeatherObject(null,null,zipcode);

        // Now we have a String representing the complete forecast in JSON Format.
        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
        // into an Object hierarchy for us.

        // These are the names of the JSON objects that need to be extracted.

        // Location information
        final String OWM_CITY = "city";
        final String OWM_CITY_NAME = "name";
        final String OWM_COORD = "coord";

        // Location coordinate
        final String OWM_LATITUDE = "lat";
        final String OWM_LONGITUDE = "lon";

        // Weather information.  Each day's forecast info is an element of the "list" array.
        final String OWM_LIST = "list";

        final String OWM_PRESSURE = "pressure";
        final String OWM_HUMIDITY = "humidity";
        final String OWM_WINDSPEED = "speed";
        final String OWM_WIND_DIRECTION = "deg";

        // All temperatures are children of the "temp" object.
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";

        final String OWM_WEATHER = "weather";
        final String OWM_MAIN = "main";
        final String OWM_DESCRIPTION = "description";
        final String OWM_WEATHER_ID = "id";
        final String OWM_MESSAGE_CODE = "cod";

        try {
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            if(forecastJson.has(OWM_MESSAGE_CODE)){
                int messageCode = forecastJson.getInt(OWM_MESSAGE_CODE);
                switch (messageCode){
                    case HttpURLConnection.HTTP_OK:
                        break;
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        WeatherAPI.setLocationStatus(context, WeatherAPI.LOCATION_STATUS_INVALID);
                        return listWeather;
                    default:
                        WeatherAPI.setLocationStatus(context, WeatherAPI.LOCATION_STATUS_SERVER_DOWN);
                        return listWeather;
                }
            }
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            JSONObject cityJson = forecastJson.getJSONObject(OWM_CITY);
            String cityName = cityJson.getString(OWM_CITY_NAME);

            JSONObject cityCoord = cityJson.getJSONObject(OWM_COORD);
            double cityLatitude = cityCoord.getDouble(OWM_LATITUDE);
            double cityLongitude = cityCoord.getDouble(OWM_LONGITUDE);


            // OWM returns daily forecasts based upon the local time of the city that is being
            // asked for, which means that we need to know the GMT offset to translate this data
            // properly.

            // Since this data is also sent in-order and the first day is always the
            // current day, we're going to take advantage of that to get a nice
            // normalized UTC date for all of our weather.

            Time dayTime = new Time();
            dayTime.setToNow();

            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            // now we work exclusively in UTC
            dayTime = new Time();

            ArrayList<Forecast> list = new ArrayList<>();

            for(int i = 0; i < weatherArray.length(); i++) {
                // These are the values that will be collected.
                long dateTime;
                double pressure;
                int humidity;
                double windSpeed;
                double windDirection;

                double high;
                double low;

                int weatherId;

                // Get the JSON object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                // Cheating to convert this to UTC time, which is what we want anyhow
                dateTime = dayTime.setJulianDay(julianStartDay+i);

                pressure = dayForecast.getDouble(OWM_PRESSURE);
                humidity = dayForecast.getInt(OWM_HUMIDITY);
                windSpeed = dayForecast.getDouble(OWM_WINDSPEED);
                windDirection = dayForecast.getDouble(OWM_WIND_DIRECTION);

                // Description is in a child array called "weather", which is 1 element long.
                // That element also contains a weather code.
                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                String main = weatherObject.getString(OWM_MAIN);
                String description = weatherObject.getString(OWM_DESCRIPTION);
                // description = Utility.getDescription(description, context);
                weatherId = weatherObject.getInt(OWM_WEATHER_ID);

                // Temperatures are in a child object called "temp".
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                high = temperatureObject.getDouble(OWM_MAX);
                low = temperatureObject.getDouble(OWM_MIN);

                Forecast forecast = new Forecast(weatherId, Utility.getFriendlyDayString(context, dateTime),
                        Utility.getArtResourceForWeatherCondition(weatherId),Utility.getAnimationResourceForWeatherCondition(weatherId),
                        high,low,main,description,pressure,(float)humidity,windSpeed,windDirection);

                list.add(forecast);

            }

            listWeather = new WeatherObject(list,cityName,zipcode);
            Log.d(LOG_TAG,"City added = " + cityName);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            WeatherAPI.setLocationStatus(context, WeatherAPI.LOCATION_STATUS_SERVER_INVALID);
            e.printStackTrace();
        }
        return listWeather;
    }
}
