package hu.ait.weatherinfo.data;

import android.media.Image;

/**
 * Created by Mack on 5/5/2017.
 */

public class City {

    private String title;
    private String description;
    private Double temperature;
    private Double minTemp;
    private Double maxTemp;
    private Double humidity;
    private String sunrise;
    private String sunset;
    private String imgUrl;

    public City(String title, String desc, Double tempNow, Double minTemp, Double maxTemp, Double hum, String sunrise, String sunset, String imgUrl) {
        this.title = title;
        this.description = desc;
        this.temperature = tempNow;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.humidity = hum;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Double minTemp) {
        this.minTemp = minTemp;
    }

    public Double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
