## Introduction

An android Sdk which provide GoolgeMap intergration with openWeather api.
## How to import
 First import the AAR file to your project  File->New->New Module->import .Jar/.AAR Package 
 then you need to edit the build.gradle file, add this two line
  compile project(':weatherSdk')
  compile 'com.google.android.gms:play-services:8.4.0'
  At Last you need change your package name to "omarlee.myweather" so that it can use the Google map api-key
## How to use
  this sdk right now provide three functions <br/>
  1  **WeatherService:** pulls weather data from openweather api by location<br/>
  ex:  ```WeatherService al = new WeatherService(YourActivity);```<br/>
      ``` al.getWeather(LatLng location);```  call the service method and give a LatLng location<br/>
  After call the weatherService, you will get the WeatherData on the call back method **serviceSuccess(WeatherData weatherData)**
  where u can use the weather Data.
      
  2 **Weather ServiceByName:** pulls weather data from openweather api by city name:<br/>
   ex:  ```WeatherServiceByName al = new WeatherServiceByName(YourActiviy);```<br/>
      ``` al.getWeather(String CityName);```call the service method and give a city name<br/>
  After call the weatherService, you will get the WeatherData on the call back method **serviceSuccess(WeatherData weatherData)**
  where u can use the weather Data.<br/>
  
  3  **MapsActivity:**  An Map activity which intergrate google map with Weather Data

 weather Data type:  
   ``` get/set  temp; ```     get/set  location temperature <br/>
    ```get/set  name;  ```    get/set  location name<br/>
  ```  get/set   id; ```      get/set weather icon id<br/>
   ``` get/set   weather;```  get/set  weather condtion<br/>
## User Example
First opent the app, will direct user to current location.
when a user click on the map, it will show a weather icon to that location,  after click the weather icon it will show more weather deatails for that location(Right now only shows **Location name, weather type, and temperature**). 
The user would be able to interact with the map (zoom, pan, etc),  app work in both portrait and landscape mode.<br/>
![alt tag](https://raw.githubusercontent.com/louisli1989/weatherMap/master/screenshot2.png)
![alt tag](https://raw.githubusercontent.com/louisli1989/weatherMap/master/screenshot3.png)


## API Reference
need support Google Maps Android API v2
target API 23

