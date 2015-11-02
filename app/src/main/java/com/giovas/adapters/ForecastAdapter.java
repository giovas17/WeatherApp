package com.giovas.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.giovas.objects.Forecast;
import com.giovas.utils.Utility;
import com.giovas.weatherapp.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by DarkGeat on 11/1/2015.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private Context mContext;
    private List<Forecast> items;

    public ForecastAdapter(Context context, List<Forecast> forecasts){
        mContext = context;
        items = forecasts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.forecast_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Glide.with(mContext).load(items.get(position).getAnimation())
                .asGif()
                .placeholder(items.get(position).getImage())
                .error(items.get(position).getImage())
                .fitCenter()
                .into(holder.iconWeather);

        holder.tday.setText(items.get(position).getDate());
        holder.tmax.setText(Utility.formatTemperature(mContext, items.get(position).getMax_temp(), true));
        holder.tmin.setText(Utility.formatTemperature(mContext,items.get(position).getMin_temp(),true));
        holder.weather.setText(items.get(position).getShortDesc());
        holder.description.setText(items.get(position).getDescription());
        holder.humidity.setText(mContext.getString(R.string.format_humidity, items.get(position).getHumidity()));
        holder.pressure.setText(mContext.getString(R.string.format_pressure, items.get(position).getPressure()));
        holder.windSpeed.setText(Utility.getFormattedWind(mContext,(float)items.get(position).getWindSpeed(),(float)items.get(position).getWindDirection()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView tday;
        public final TextView tmax;
        public final TextView tmin;
        public final ImageView iconWeather;
        public final TextView weather;
        public final TextView description;
        public final TextView humidity;
        public final TextView pressure;
        public final TextView windSpeed;

        public ViewHolder(View view){
            super(view);
            iconWeather = (ImageView)view.findViewById(R.id.imageIconWeather);
            tday = (TextView) view.findViewById(R.id.textDay);
            weather = (TextView) view.findViewById(R.id.textWeather);
            tmax = (TextView) view.findViewById(R.id.textMaxTemp);
            tmin = (TextView) view.findViewById(R.id.textMinTemp);
            description = (TextView) view.findViewById(R.id.descriptionWeather);
            humidity = (TextView) view.findViewById(R.id.detail_humidity_text);
            pressure = (TextView) view.findViewById(R.id.detail_pressure_text);
            windSpeed = (TextView) view.findViewById(R.id.detail_wind_text);
        }

    }
}
