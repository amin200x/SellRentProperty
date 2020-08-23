package com.amin.sellrenthouse.launch;

import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;



public class HouseOnMapActivity extends FragmentActivity /*implements OnMapReadyCallback*/ {
/*
    private  Location mLocation;
    private  Location hLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_on_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
         mapFragment.getMapAsync(this);

         mLocation = this.getIntent().getParcelableExtra("mLocation");
         hLocation = this.getIntent().getParcelableExtra("hLocation");

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
               LatLng hLatLng = new LatLng(hLocation.getLatitude(), hLocation.getLongitude());
        LatLng mLatLng ;
        if (mLocation != null){
            //System.out.println("D mLocation: " + mLocation);
            mLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(mLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("مکان من"));
        }
        googleMap.addMarker(new MarkerOptions().position(hLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("مکان ملک"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(hLatLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
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
