package com.giovas.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.text.format.Time;

import com.giovas.weatherapi.R;
import com.giovas.weatherapi.WeatherAPI;

import java.text.SimpleDateFormat;

/**
 * Created by DarkGeat on 10/31/2015.
 */
public class Utility {

    public static final String DATE_FORMAT = "yyyyMMdd";

    public static String formatTemperature(Context mContext, double temperature, boolean isMetric) {
        double temp;
        if (!isMetric){
            temp = 9 * temperature / 5 + 52;
        }else{
            temp = temperature;
        }
        return mContext.getString(R.string.format_temperature, temp);
    }

    public static String getFriendlyDayString(Context context, long dateInMillis) {
        // The day string for forecast uses the following logic:
        // For today: "Today, June 8"
        // For tomorrow:  "Tomorrow"
        // For the next 5 days: "Wednesday" (just the day name)
        // For all days after that: "Mon Jun 8"

        Time time = new Time();
        time.setToNow();
        long currentTime = System.currentTimeMillis();
        int julianDay = Time.getJulianDay(dateInMillis, time.gmtoff);
        int currentJulianDay = Time.getJulianDay(currentTime, time.gmtoff);

        // If the date we're building the String for is today's date, the format
        // is "Today, June 24"
        if (julianDay == currentJulianDay) {
            String today = context.getString(R.string.today);
            int formatId = R.string.format_full_friendly_date;
            return String.format(context.getString(
                    formatId,
                    today,
                    getFormattedMonthDay(context, dateInMillis)));
        } else if (julianDay < currentJulianDay + 7) {
            // If the input date is less than a week in the future, just return the day name.
            return getDayName(context, dateInMillis);
        } else {
            // Otherwise, use the form "Mon Jun 3"
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return CapitalizeDate(shortenedDateFormat.format(dateInMillis));
        }

    }

    /**
     * Given a day, returns just the name to use for that day.
     * E.g "today", "tomorrow", "wednesday".
     *
     * @param context Context to use for resource localization
     * @param dateInMillis The date in milliseconds
     * @return
     */
    public static String getDayName(Context context, long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if ( julianDay == currentJulianDay +1 ) {
            return context.getString(R.string.tomorrow);
        } else {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            String actualDay = CapitalizeFirstLetter(dayFormat.format(dateInMillis));
            return actualDay;
        }
    }

    /**
     * Converts db date format to the format "Month day", e.g "June 24".
     * @param context Context to use for resource localization
     * @param dateInMillis The db formatted date string, expected to be of the form specified
     *                in Utility.DATE_FORMAT
     * @return The day in the form of a string formatted "December 6"
     */
    public static String getFormattedMonthDay(Context context, long dateInMillis ) {
        Time time = new Time();
        time.setToNow();
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(Utility.DATE_FORMAT);
        SimpleDateFormat monthDayFormat = new SimpleDateFormat("MMMM dd");
        String monthDayString = CapitalizeFirstLetter(monthDayFormat.format(dateInMillis));
        return monthDayString;
    }

    /**
     * Helper method to provide the icon resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     * @param weatherId from OpenWeatherMap API response
     * @return resource id for the corresponding icon. -1 if no relation is found.
     */
    public static int getAnimationResourceForWeatherCondition(int weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.art_storm_animated;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.art_light_rain_animated;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.art_rain_animated;
        } else if (weatherId == 511) {
            return R.drawable.art_snow_animated;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.art_rain_animated;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.art_snow_animated;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.art_fog_animated;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.art_storm_animated;
        } else if (weatherId == 800) {
            return R.drawable.art_clear_animated;
        } else if (weatherId == 801) {
            return R.drawable.art_light_clouds_animated;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.art_clouds_animated;
        }
        return -1;
    }

    /**
     * Helper method to provide the art resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     * @param weatherId from OpenWeatherMap API response
     * @return resource id for the corresponding icon. -1 if no relation is found.
     */
    public static int getArtResourceForWeatherCondition(int weatherId) {

        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.art_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.art_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.art_rain;
        } else if (weatherId == 511) {
            return R.drawable.art_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.art_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.art_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.art_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.art_storm;
        } else if (weatherId == 800) {
            return R.drawable.art_clear;
        } else if (weatherId == 801) {
            return R.drawable.art_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.art_clouds;
        }
        return -1;
    }

    private static String CapitalizeFirstLetter(String charSequence){
        return (charSequence.substring(0,1).toUpperCase() + charSequence.substring(1));
    }

    private static String CapitalizeDate(String chartSquence){
        String day = chartSquence.substring(0,1).toUpperCase() + chartSquence.substring(1,3);
        String aux = chartSquence.substring(3);
        String month = aux.substring(0,2).toUpperCase() + aux.substring(2,3);
        aux = aux.substring(3);
        return day + month + aux;
    }


    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    @SuppressWarnings("ResourceType")
    public static @WeatherAPI.LocationStatus int getLocationStatus(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(context.getString(R.string.pref_loc_status), WeatherAPI.LOCATION_STATUS_UNKNOWN);
    }

    public static void resetLocationStatus(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(context.getString(R.string.pref_loc_status),WeatherAPI.LOCATION_STATUS_UNKNOWN);
        editor.apply();
    }

    public static String getFormattedWind(Context context, float windSpeed, float degrees){
        int windFormat;
        windFormat = R.string.format_wind_kmh;
        /*if (Utility.isMetric(context)) {
            windFormat = R.string.format_wind_kmh;
        } else {
            windFormat = R.string.format_wind_mph;
            windSpeed = .621371192237334f * windSpeed;
        }*/

        // From wind direction in degrees, determine compass direction as a string (e.g NW)
        // You know what's fun, writing really long if/else statements with tons of possible
        // conditions.  Seriously, try it!
        String direction = "Unknown";
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = context.getString(R.string.north);
        } else if (degrees >= 22.5 && degrees < 67.5) {
            direction = context.getString(R.string.north_east);
        } else if (degrees >= 67.5 && degrees < 112.5) {
            direction = context.getString(R.string.east);
        } else if (degrees >= 112.5 && degrees < 157.5) {
            direction = context.getString(R.string.south_east);
        } else if (degrees >= 157.5 && degrees < 202.5) {
            direction = context.getString(R.string.south);
        } else if (degrees >= 202.5 && degrees < 247.5) {
            direction = context.getString(R.string.south_west);
        } else if (degrees >= 247.5 && degrees < 292.5) {
            direction = context.getString(R.string.west);
        } else if (degrees >= 292.5 && degrees < 337.5) {
            direction = context.getString(R.string.north_west);
        }
        return String.format(context.getString(windFormat), windSpeed, direction);
    }

}
