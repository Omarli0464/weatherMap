package omarlee.weathermap;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by omar on 4/25/16.
 */
public class WeatherService {
    private WeatherServiceListener listener;
    private Exception error;
    public WeatherService(WeatherServiceListener listener){
        this.listener=listener;
    }

    public void getWeather(LatLng location){
        new AsyncTask<LatLng,Void,WeatherData>(){
            @Override
            protected WeatherData doInBackground(LatLng...params) {
                WeatherData weatherData= new WeatherData();
                Double lat= params[0].latitude;
                Double lon=  params[0].longitude;
                String endpoint =String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=imperial&APPID=72a7923f3cb9f652c63d51c9585379b0",lat,lon);
                try{
                    URL url=  new URL(endpoint);
                    URLConnection connection= url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result =  new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        result.append(line);
                    }
                    JSONObject data = new JSONObject(result.toString());
                    weatherData.setTemp(data.optJSONObject("main").optDouble("temp"));
                    weatherData.setWeather(data.optJSONArray("weather").getJSONObject(0).optString("main"));
                    weatherData.setName(data.optString("name"));
                    weatherData.setId(data.optJSONArray("weather").getJSONObject(0).optString("icon"));
                    return weatherData;

                }
                catch(Exception e){
                    error=e;
                    Log.d(error.toString(),"shibaile");
                }

                return null;
            }

            @Override
            protected void onPostExecute(WeatherData weatherData) {
                if(weatherData==null && error !=null){
                    listener.serviceFailure(error);
                }else {
                    listener.serviceSuccess(weatherData);
                }
            }
        }.execute(location);
    }
}
