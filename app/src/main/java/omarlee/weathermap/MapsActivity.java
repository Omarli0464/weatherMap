package omarlee.weathermap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,ActivityCompat.OnRequestPermissionsResultCallback,
        OnMapClickListener,WeatherServiceListener{
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;
    protected GoogleMap mMap;
    protected LatLng  mlocation;
    private  WeatherService weatherService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);// Obtain the SupportMapFragment and get notified when the map is ready to be used.


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();
        mMap.setOnMapClickListener(this);

    }
    @Override
    public void onMapClick(LatLng point) {
        Log.d("DEBUG","Map clicked [" + point.latitude + " / " + point.longitude+"]");
        mlocation=point;
        weatherService=new WeatherService(this);
        weatherService.getWeather(mlocation);


    }
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,
                                           int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    class CustomInfoWindowAdapter implements InfoWindowAdapter {

        // These a both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;
        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
         render(marker,mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {

           render(marker,mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {



            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView weather = ((TextView) view.findViewById(R.id.snippet));
            TextView temp = ((TextView) view.findViewById(R.id.temp));

            if (snippet != null) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 5, 0);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLACK), 5, 11, 0);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 11, snippet.length(), 0);
                temp.setText(snippetText.subSequence(0,11));
               weather.setText(snippetText.subSequence(11,snippet.length()));
            } else {
                weather.setText("");
                temp.setText("");
            }
        }
    }

    @Override
    public void serviceSuccess(WeatherData wd) {
          mMap.clear();
       int resourceId = getResources().getIdentifier("drawable/icon_"+wd.getId(), null, getPackageName());
        Log.d("DEBUG","id received [" +wd.getId()+"]");
        mMap.addMarker(new MarkerOptions().position(mlocation).snippet("Temp:"+wd.getTemp()+" "+wd.getWeather()).title(wd.getName()).icon(BitmapDescriptorFactory.fromResource(resourceId)));
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mlocation));
        Log.d("DEBUG","weather received [" +wd.getWeather()+wd.getTemp()+wd.getName()+"]");
    }

    @Override
    public void serviceFailure(Exception e) {
        Log.d("DEBUG","weather failed");
    }
}
