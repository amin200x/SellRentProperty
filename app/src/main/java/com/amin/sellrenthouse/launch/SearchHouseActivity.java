package com.amin.sellrenthouse.launch;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.*;
import android.support.annotation.RequiresApi;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.amin.sellrenthouse.R;
import com.amin.sellrenthouse.constants.HouseType;
import com.amin.sellrenthouse.constants.SellRent;
import com.amin.sellrenthouse.constants.SpinnerData;
import com.amin.sellrenthouse.controller.HouseController;
import com.amin.sellrenthouse.controller.ProvinceCityController;
import com.amin.sellrenthouse.entities.City;
import com.amin.sellrenthouse.entities.County;
import com.amin.sellrenthouse.entities.House;
import com.amin.sellrenthouse.entities.Province;
import com.amin.sellrenthouse.manager.HouseManager;
import com.amin.sellrenthouse.utils.CustomAdapter;
import com.amin.sellrenthouse.utils.MySpinnerAdapter;
import com.amin.sellrenthouse.utils.UserLogHelper;

import com.ibm.icu.util.Calendar;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class SearchHouseActivity extends AppCompatActivity /*implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener*/
{
   //private GoogleApiClient mGoogleApiClient;
   private Location mLocation;
   private LocationManager mLocationManager;
   private int days = -1;
   private List<House> houseList;
   private HouseType type = null;
   private SellRent sellRent = null;
   private EditText maxSizeEditText;
   private EditText ageEditText;
   private EditText priceEditText;
   private EditText rentalCostEditText;
   private EditText distanceEditText;
   private AutoCompleteTextView acProvinceTextView;
   private AutoCompleteTextView acCountyTextView;
   private AutoCompleteTextView acCityTextView;
   private ListView listView;
   private RelativeLayout searchRelativelayout;
   private Button searchButton;
   private InputMethodManager mgr;
   private ScrollView settingsScrollView;
   private Map<String, Integer> mapProvince = null;
   private Map<String, Integer> countyMap = null;
   private Map<String, Integer> cityMap = null;
   private ArrayAdapter<String> provinceAdapter;
   private ArrayAdapter<String> countyAdapter;
   private Province province = null;
   private County county = null;
   private City city = null;
   private ArrayAdapter<String> cityAdapter;
   private ProgressBar progressBarSearchHouse;
   /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

            if(requestCode == REQUEST_USER_LOCATION_CODE){
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(i, 1);
                    }
                }else {

                    Toast.makeText(this, "رد مجوز!!", Toast.LENGTH_LONG).show();
                }

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
*/
    public void assignSettings(View view){
        settingsScrollView.setVisibility(View.INVISIBLE);
        searchRelativelayout.setVisibility(View.VISIBLE);

    }
    public void showSettings(View view){
        settingsScrollView.setVisibility(View.VISIBLE);
        searchRelativelayout.setVisibility(View.INVISIBLE);
    }
      @TargetApi(Build.VERSION_CODES.N)
      @RequiresApi(api = Build.VERSION_CODES.N)
      public void search(View view){
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mgr.hideSoftInputFromWindow(searchButton.getWindowToken(),0);
        if (houseList != null)
            houseList.clear();
       if (province == null){
           Toast.makeText(this, "استان را تعیین کنید!", Toast.LENGTH_SHORT).show();
           return;
       }
        else  if (county == null){
            Toast.makeText(this, "شهرستان را تعیین کنید!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (city == null){
              city = new City(0, "");
          }
          int size = Integer.valueOf ((!maxSizeEditText.getText().toString().isEmpty()) ? maxSizeEditText.getText().toString(): "0");;
        int age = Integer.valueOf((!ageEditText.getText().toString().isEmpty()) ? ageEditText.getText().toString(): "0");
        long price = Long.valueOf((!priceEditText.getText().toString().isEmpty()) ? priceEditText.getText().toString(): "0");
        long rentalCost = Long.valueOf((!rentalCostEditText.getText().toString().isEmpty()) ? rentalCostEditText.getText().toString(): "0");
        double distance = Double.valueOf((!distanceEditText.getText().toString().isEmpty()) ? distanceEditText.getText().toString(): "0.0");

        House house = HouseManager.getInstance().createHouse(-1, "", type, -1, size, price, "",
                mLocation, -1, sellRent, null, province, county, city, age, rentalCost);
        final Date currentDate = Calendar.getInstance().getTime();
        // TimeUnit.DAYS.convert(currentDate.getTime() - registerDate.getTime().getTime(), TimeUnit.MILLISECONDS);

              new Thread(new Runnable() {
                  @Override
                  public void run() {
                      try{
                          runOnUiThread(() ->{
                              progressBarSearchHouse.setVisibility(View.VISIBLE);
                          });
                          houseList =  HouseController.getInstance().search(house);

                      } catch (ExecutionException | InterruptedException e) {
                      showMessage("خطا در شبکه!");

                  }
                      runOnUiThread(() ->{
                          progressBarSearchHouse.setVisibility(View.GONE);
                          if (days > -1)
                              houseList = houseList.stream()
                                      .filter((h) -> ( TimeUnit.DAYS.convert(currentDate.getTime() - h.getRegisterDate().getTime().getTime(), TimeUnit.MILLISECONDS)) <= days)
                                      .collect(Collectors.toList());
                          if (distance > 0 && mLocation != null)
                              // mile * 1.609344 = km
                              houseList = houseList.stream()
                                      .filter((h) -> (h.getLocation().distanceTo(mLocation) * 1.609344 / 1000 )  <= distance)
                                      .collect(Collectors.toList());

                          if (houseList.size() == 0){
                              showMessage("موردی یافت نشد!");
                          }


                          CustomAdapter customAdapter = new CustomAdapter(SearchHouseActivity.this, houseList);
                          listView.setAdapter(customAdapter);

                      });


                  }
              }).start();

              //List<House> list;


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_house);
        houseList = new ArrayList<>();
        Spinner houseTypesSpinner = findViewById(R.id.housetypeSpinner);
        maxSizeEditText = findViewById(R.id.sizeEditText);
       progressBarSearchHouse = findViewById(R.id.progressBarSearchHouse);
        progressBarSearchHouse.setVisibility(View.GONE);
        ageEditText = findViewById(R.id.ageEditText);

        priceEditText = findViewById(R.id.priceEditText);

        rentalCostEditText = findViewById(R.id.rentalCostEditText);

        distanceEditText = findViewById(R.id.distanceEditText);

        acProvinceTextView = findViewById(R.id.provinceCompleteTextView);
        acCountyTextView = findViewById(R.id.countyAutoCompleteTextView);
        acCityTextView = findViewById(R.id.cityAutoCompleteTextView);
        listView = findViewById(R.id.listView);

        searchButton = findViewById(R.id.searchButton);
        searchRelativelayout = findViewById(R.id.searchRelativeLayout);
        Spinner sellRentSpinner = findViewById(R.id.sellRentSpinner);
        Spinner daySpinner = findViewById(R.id.daySpinner);
        settingsScrollView = findViewById(R.id.settingsScrollView);
        settingsScrollView.setVisibility(View.INVISIBLE);
        searchRelativelayout.setVisibility(View.VISIBLE);
        mgr = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        acProvinceTextView.requestFocus();
        maxSizeEditText.requestFocus();

        acCityTextView.setVisibility(View.INVISIBLE);
        acCountyTextView.setVisibility(View.INVISIBLE);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final String[] daysArr = {"انتخاب کنید", "امروز", "چند روز پیش", "یک هفته پیش", "یک ماه پیش", "سه ماه پیش", "شش ماه پیش", "یک سال پیش"};
        final ArrayAdapter<String> daysAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Arrays.asList(daysArr));
        daySpinner.setAdapter(daysAdapter);

           acCountyTextView.setOnClickListener((v) -> {
            city = null;
            county = null;
        });
        acCityTextView.setOnClickListener((v) -> city = null);
        acProvinceTextView.setOnClickListener((v) -> {
            province = null;
            city = null;
            county = null;
        });

    loadProvinces();


        acCityTextView.setOnItemClickListener((parent, view, position, id) -> {
            int cityId = cityMap.get(acCityTextView.getText().toString());
            city = new City(cityId, acCityTextView.getText().toString());
        });
        acCountyTextView.setOnItemClickListener((parent, view, positon, id) -> {
            acCityTextView.setVisibility(View.INVISIBLE);
            acCityTextView.setText("");
            city = null;
           new Thread(new Runnable() {
               @Override
               public void run() {

                   try {
                       int countyId = countyMap.get(acCountyTextView.getText().toString());
                       county = new County(countyId, acCountyTextView.getText().toString());
                       cityMap = ProvinceCityController.getCities(countyId);
                       List<String> selectedCities = new ArrayList<>(cityMap.keySet());
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               cityAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, selectedCities);
                               acCityTextView.setAdapter(cityAdapter);
                               acCityTextView.requestFocus();
                               acCityTextView.setVisibility(View.VISIBLE);
                               acCountyTextView.setVisibility(View.VISIBLE);
                           }
                       });

                   } catch (ExecutionException | InterruptedException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showMessage("خطا در شبکه!");
                            }
                        });

                   }
               }
           }).start();

        });
        acProvinceTextView.setOnItemClickListener((parent, view, position, id) -> {
            acCityTextView.setVisibility(View.INVISIBLE);
            acCountyTextView.setVisibility(View.INVISIBLE);
            acCountyTextView.setText("");
            acCityTextView.setText("");
            county = null;
            city = null;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mapProvince.size() == 0)
                            loadProvinces();
                        int provinceId = mapProvince.get(acProvinceTextView.getText().toString());
                        province = new Province(provinceId, acProvinceTextView.getText().toString());
                        countyMap = ProvinceCityController.getCounties(provinceId);
                        List<String> selectedCounties = new ArrayList<>(countyMap.keySet());
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             countyAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, selectedCounties);
                             acCountyTextView.setAdapter(countyAdapter);
                             acCountyTextView.requestFocus();
                             acCountyTextView.setVisibility(View.VISIBLE);
                         }
                     });

                    } catch (ExecutionException | InterruptedException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showMessage("خطا در شبکه!");
                            }
                        });

                    }
                }
            }).start();


        });
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        days = 0;
                        break;
                    case 2:
                        days = 4;
                        break;
                    case 3:
                        days = 7;
                        break;
                    case 4:
                        days = 31;
                        break;
                    case 5:
                        days = 90;
                        break;
                    case 6:
                        days = 180;
                        break;
                    case 7:
                        days = 365;
                        break;
                    default:
                        days = -1;


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        MySpinnerAdapter mySpinnerAdapter = new MySpinnerAdapter(this, SpinnerData.getInstance().getSpinnerItems(), SpinnerData.getInstance().getImages());

        ArrayAdapter<String> sellRentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Arrays.asList("همه", "خرید", "اجاره"));
        sellRentSpinner.setAdapter(sellRentAdapter);
        houseTypesSpinner.setAdapter(mySpinnerAdapter);

        sellRentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    sellRent = SellRent.values()[position - 1];
                } else sellRent = null;

                if (sellRent == SellRent.RENT) {
                    rentalCostEditText.setEnabled(true);
                    priceEditText.setHint("پیش پرداخت");
                } else {
                    rentalCostEditText.setEnabled(false);
                    priceEditText.setHint("قیمت");

                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        houseTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0)
                    type = HouseType.values()[position - 1];
                else
                    type = null;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listView.setOnItemClickListener( (parent,  view,  position,  id) -> {
             {
                Intent intent = new Intent(getApplicationContext(), HouseDetailActivity.class);
                intent.putExtra("house", houseList.get(position));
                intent.putExtra("mLocation", mLocation);
                startActivity(intent);
            }
        });

       /* mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        checkLocationEnabeling();

        */

    }

    private void loadProvinces() {
        new Thread(() -> {

            try {

                mapProvince = ProvinceCityController.getProvinces();
                while (mapProvince.size() == 0)
                    mapProvince = ProvinceCityController.getProvinces();
                runOnUiThread(() ->{
                    List<String> list = new ArrayList<>(mapProvince.keySet());
                    provinceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                    acProvinceTextView.setAdapter(provinceAdapter);
                });
            } catch (ExecutionException e) {
                showMessage("خطا در شبکه!");
            } catch (InterruptedException e) {
                showMessage("خطا در شبکه!");
            }
        }).start();
    }


    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (resultCode == RESULT_CANCELED) {
             checkLocationEnabeling();
       }
        }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        startLocationUpdates();
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation == null){
            startLocationUpdates();
        }

    }

    private void startLocationUpdates() {
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1100)
                .setFastestInterval(1100);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
        return;

    }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Log.d("reque", "--->>>>");

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Error: ", "Connection failed. Error: " + connectionResult.getErrorCode());


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }


   */
/*
    @Override
    public void onLocationChanged(Location location) {
        System.out.println("My location in location change method: " + mLocation);

    }


    private boolean checkLocationEnabeling(){

        if (!isLocationEnabled()){
            distanceEditText.setEnabled(false);
            showAlert();
        }else {
               distanceEditText.setEnabled(true);

        }
        return isLocationEnabled();
    }

    private void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("تنضیمات مکانیاب غیر فعال است.")
                    .setMessage("جستجو براساس فاصله.")
                    .setPositiveButton("تایید مجوز مکانیاب", ( dialog1,  which)-> {
                        {
                            boolean isGranted = false;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                isGranted = checkUserLocationPermisson();

                            }
                            if (isGranted){
                                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(i, 1);
                            }

                        }
                    })
                    .setNegativeButton("لغو", ( dialog1,  which)->  {
                      {
                        dialog1.dismiss();
                        }
                    });
            dialog.show();
    }

    private boolean isLocationEnabled() {
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


     */
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item;

        if (new UserLogHelper(this).isLogedIn()){
            item = menu.findItem(R.id.login);
            item.setVisible(false);
            item = menu.findItem(R.id.logout);
            item.setVisible(true);
            item = menu.findItem(R.id.home);
            item.setVisible(true);
        }else {
            item = menu.findItem(R.id.login);
            item.setVisible(true);
            item = menu.findItem(R.id.logout);
            item.setVisible(false);
            item = menu.findItem(R.id.home);
            item.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int id = item.getItemId();
        switch (id){
            case R.id.login:
                intent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            case R.id.logout:
                if (new UserLogHelper(this).isLogedIn()){
                    new AlertDialog.Builder(this)
                            .setIcon(R.drawable.logout_icon)
                            .setMessage("از حساب کاربری خارج می شوید؟")
                            .setPositiveButton("بله", (d, w) -> {
                                new UserLogHelper(SearchHouseActivity.this).logout();

                            })
                            .setNegativeButton("خیر", (d, v) -> {

                            }).show();
                }
                return true;

            case R.id.search:
                this.finish();
                intent = new Intent(getApplicationContext(), SearchHouseActivity.class);
                startActivity(intent);
                return true;
            case R.id.home:
                this.finish();
                intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                this.finish();
                return true;
                case R.id.about:
                    showAboutDialog();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void showAboutDialog(){
        AlertDialog.Builder aboutDialog = new  AlertDialog.Builder(this);
        TextView contentTextView = new TextView(this);
        ImageView imageView = new ImageView(this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        imageView.setImageResource(R.drawable.home);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(250, 250));
        contentTextView.setTextSize( 20f);
        contentTextView.setGravity(Gravity.CENTER);
        contentTextView.setText(R.string.about);
        linearLayout.addView(imageView);
        linearLayout.addView(contentTextView);
        aboutDialog.setView(linearLayout)

                .setTitle("در باره بر تا‍پ")
                .setCancelable(false)
                .setPositiveButton("تایید", (dialog,  which) -> dialog.dismiss()).show();


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
              this.finish();
              System.exit(0);
           return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
