package hu.ait.weatherinfo.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import hu.ait.weatherinfo.MainActivity;
import hu.ait.weatherinfo.R;
import hu.ait.weatherinfo.data.City;

import static com.google.gson.internal.bind.TypeAdapters.UUID;

/**
 * Created by Mack on 5/5/2017.
 */

public class CityRecyclerAdaptor
        extends RecyclerView.Adapter<CityRecyclerAdaptor.ViewHolder> {


    private final String imgUrlStart = "http://openweathermap.org/img/w/";


    private List<City> cityList;

    private Context context;

    public CityRecyclerAdaptor(Context context) {
        this.context = context;
        cityList = new ArrayList<City>();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.city_row,
                parent,
                false
        );
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.titleView.setText(cityList.get(position).getTitle());
        holder.tempView.setText(Double.toString(cityList.get(position).getTemperature()));
        holder.descView.setText(cityList.get(position).getDescription());
        holder.minTView.setText(Double.toString(cityList.get(position).getMinTemp()));
        holder.maxTView.setText(Double.toString(cityList.get(position).getMaxTemp()));
        String fullImgUrl = imgUrlStart + (cityList.get(position).getImgUrl()) + context.getString(R.string.urlEnd);
        Glide.with(context).load(fullImgUrl).into(holder.iconView);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                cityList.remove(pos);
                notifyItemRemoved(pos);
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                City city = cityList.get(pos);
                ((MainActivity) context).launchDetailsActivity(city.getTitle(), city.getDescription(), city.getTemperature(), city.getMinTemp(), city.getMaxTemp(), city.getHumidity(), city.getSunrise(), city.getSunset());
            }
        });

    }

    public int getItemCount() {
        return cityList.size();
    }

    public void addCity(City city) {
//        realmCity.beginTransaction();
        cityList.add(0, city);
        notifyItemInserted(0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleView;
        private TextView tempView;
        private TextView descView;
        private TextView minTView;
        private TextView maxTView;
        private ImageView iconView;
        private ImageView deleteButton;

        public ViewHolder(View rowView) {
            super(rowView);

            titleView = (TextView) rowView.findViewById(R.id.cityTitle);
            tempView = (TextView) rowView.findViewById(R.id.cityTemp);
            descView = (TextView) rowView.findViewById(R.id.descPlaceHolder);
            minTView = (TextView) rowView.findViewById(R.id.minTPlaceHolder);
            maxTView = (TextView) rowView.findViewById(R.id.maxTPlaceHolder);
            iconView = (ImageView) rowView.findViewById(R.id.iconPlaceHolder);
            deleteButton = (ImageView) rowView.findViewById(R.id.deleteCity);
        }
    }

    public void clearAll() {
        for (int i = cityList.size() - 1; i >= 0; i--) {
            cityList.remove(i);
            notifyItemRemoved(i);
        }
    }
}
