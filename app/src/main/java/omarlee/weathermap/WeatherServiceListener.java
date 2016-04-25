package omarlee.weathermap;

/**
 * Created by omar on 4/25/16.
 */
public interface WeatherServiceListener {
    void serviceSuccess(WeatherData wd);
    void  serviceFailure(Exception e);

}
