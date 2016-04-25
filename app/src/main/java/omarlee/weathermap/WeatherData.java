package omarlee.weathermap;

/**
 * Created by omar on 4/25/16.
 */
public class WeatherData {
    private double temp;
    private String weather;

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setTemp(double temp) {

        this.temp = temp;
    }

    public double getTemp() {
        return temp;
    }

    public String getWeather() {
        return weather;
    }
}
