package com.amin.sellrenthouse.launch;


import android.content.Intent;
import android.location.Location;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import com.amin.sellrenthouse.R;
import com.amin.sellrenthouse.entities.House;



public class HouseLocationActivity extends FragmentActivity /*implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener*/ {

   /* private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private Marker houseLocationMarker; */
    private Location houseLocation;
    static final int REQUEST_USER_LOCATION_CODE = 99;
    Button regHouseLocationBtn;

    private  String sender;
    public void regHouseLocation(View view) {
        Intent backIntent;
        if(sender.contains("register")){
            backIntent = new Intent(getApplicationContext(), RegisterHouseActivity.class);
            if (backIntent != null){
                backIntent.putExtra("location", this.houseLocation);
                startActivity(backIntent);
            }
        }else if(sender.contains("update")){
               House house = getIntent().getParcelableExtra("house");
               house.setLocation(houseLocation);
               backIntent = new Intent(getApplicationContext(), UpdateHouseActivity.class);
               backIntent.putExtra("house" , house);
               startActivity(backIntent);
              }
           this.finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_location);
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager() .findFragmentById(R.id.map);




     // mapFragment.getMapAsync(this);
        regHouseLocationBtn = findViewById(R.id.regHouseLocationButton);

        sender = getIntent().getStringExtra("intentSender");

     /*     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkUserLocationPermisson();
        }
        setupAutoCompleteFragment();

      */
    }

  /*  @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        if (mMap != null) {
            mMap.setOnMapClickListener((point) -> {
                {
                    Location location = new Location("chosenLocation");
                    location.setLongitude(point.longitude);
                    location.setLatitude(point.latitude);
                    onLocationChanged(location);

                   }
            });
        }

    }
    public  boolean checkUserLocationPermisson(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_USER_LOCATION_CODE);
            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_USER_LOCATION_CODE);

            }
            return false;
        }else {
            return true;

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_USER_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if (googleApiClient == null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }else {
                    Toast.makeText(this, "رد مجوز!!", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .addApi(LocationServices.API)
                            .build();
        googleApiClient.connect();

    }
    @Override
    public void onLocationChanged(Location location) {

        houseLocation = location;
       if( houseLocationMarker != null){
           houseLocationMarker.remove();
       }
       LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
       MarkerOptions markerOptions = new MarkerOptions();
       markerOptions.position(latLng);
       markerOptions.title("محل مورد نظر");
       markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
       houseLocationMarker = mMap.addMarker(markerOptions);
       mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
      // mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
       if (googleApiClient != null){
           LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

       }

    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
       if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
       {
           LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }
    }
    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void setupAutoCompleteFragment() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autoComplete);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Location location = new Location("searchedLocation");
                location.setLatitude(place.getLatLng().latitude);
                location.setLongitude(place.getLatLng().longitude);

                System.out.println("My location:" + location.getLatitude() + "    " + location.getLongitude());
                onLocationChanged(location);
            }

            @Override
            public void onError(Status status) {

            }
        });

    }


   */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}