package omarlee.weathermap;

/**
 * Created by omar on 4/25/16.
 */
public class WeatherData {
    private double temp;
    private String weather;
    private String name;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
