package hu.ait.weatherinfo.network;

import hu.ait.weatherinfo.data.WeatherInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mack on 5/9/2017.
 */

public interface WeatherAPI {

    @GET("data/2.5/weather")
    Call<WeatherInfo> getWeatherForCity(@Query("q") String location, @Query("units") String units, @Query("appid") String apikey);
}
