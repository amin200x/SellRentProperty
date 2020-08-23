package com.amin.sellrenthouse.launch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.amin.sellrenthouse.utils.MySpinnerAdapter;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class RegisterHouseActivity extends AppCompatActivity  {

  private  Spinner houseTypeSpinner;
  private  Spinner sellRentSpinner;
  private  EditText floorEtext;
  private  EditText houseSizeEtext;
  private  EditText priceEtext;
  private  EditText addressEtext;
  private  EditText descriptionEtext;
  private  EditText rentalCostEText;
  private  EditText ageEtext;
  private  AutoCompleteTextView acProvinceTextView;
  private  AutoCompleteTextView acCountyTextView;
  private  AutoCompleteTextView acCityTextView;
  Button registerBtn;
  private  Province province = null;
   private County county = null;
   private City city = null;
   private int age = 0;
   private long ownerId = 0;
   private Location location = null;
    private SellRent sellRent = null;
    private HouseType houseType = null;
   private Map<String, Integer> mapProvince = null;
   private Map<String, Integer> countyMap = null;
   private Map<String, Integer> cityMap = null;
   private ArrayAdapter<String> provinceAdapter = null;
   private ArrayAdapter<String> countyAdapter = null;
   private ArrayAdapter<String> cityAdapter = null;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void registerHouse(View view) {

        if (houseType == null) {
            Toast.makeText(this, "نوع ملک را مشخص کن!!", Toast.LENGTH_LONG).show();
            return;
        }
       else if (sellRent == null) {
            Toast.makeText(this, "نوع درخواست را مشخص کن!!", Toast.LENGTH_LONG).show();
            return;
        }
       else if (houseSizeEtext.getText().toString().isEmpty()) {
            Toast.makeText(this, "متراژ را مشخص کن!!", Toast.LENGTH_LONG).show();
            return;
        }
       else if (priceEtext.getText().toString().isEmpty()) {
            Toast.makeText(this, "قیمت را مشخص کن!!", Toast.LENGTH_LONG).show();
            return;
        }
        else if (province == null) {
            Toast.makeText(this, "استان را مشخص کن!!", Toast.LENGTH_LONG).show();
            return;
        }
        else if (county == null) {
            Toast.makeText(this, "شهرستان را مشخص کن!!", Toast.LENGTH_LONG).show();
            return;
        }
        else if (city == null) {
            Toast.makeText(this, "شهر را مشخص کن!!", Toast.LENGTH_LONG).show();
            return;
        }


        int floorNumber = Integer.valueOf(((!floorEtext.getText().toString().isEmpty()) ? floorEtext.getText().toString() : "0"));
        long rentalCost = Long.valueOf(((!rentalCostEText.getText().toString().isEmpty()) ? rentalCostEText.getText().toString() : "0"));
        double houseSize = Double.valueOf(houseSizeEtext.getText().toString());
        long price = Long.valueOf(priceEtext.getText().toString());
        age = Integer.valueOf(!ageEtext.getText().toString().isEmpty() ? ageEtext.getText().toString() : "0");
        String address = addressEtext.getText().toString();
        String description = descriptionEtext.getText().toString();


       if (ownerId <= 0) {
           Toast.makeText(this, "شما وارد سیستم نشده اید!", Toast.LENGTH_LONG).show();
           startActivity(new Intent(getApplicationContext(), SigninActivity.class));
           return;
       }

      if (location == null) {
          location = new Location("tset");
          location.setLatitude(0d);
          location.setLongitude(0d);
      }
       House house = HouseManager.getInstance().createHouse(0, address, houseType, floorNumber,
                houseSize, price, description, location, ownerId, sellRent, null, province, county,
                city, age, rentalCost);

        try {
            HouseController.getInstance().registerHouse(house);
            showMessage("با موفقیت ثبت شد.");

        } catch (ExecutionException | InterruptedException e) {
            showMessage("خطا در شبکه!");
            e.printStackTrace();
            return;
        }
        cleaForm();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_house);

        houseTypeSpinner = findViewById(R.id.housetypeSpinner);
        floorEtext = findViewById(R.id.floorNumberEditText);
        houseSizeEtext = findViewById(R.id.housesizeEditText);
        priceEtext = findViewById(R.id.priceEditText);
        addressEtext = findViewById(R.id.addressEditText);
        descriptionEtext = findViewById(R.id.desciptionEditText);
        registerBtn = findViewById(R.id.registerButton);
        sellRentSpinner = findViewById(R.id.sellRentSpinner);
        rentalCostEText = findViewById(R.id.rentalCostEditText);
         ageEtext = findViewById(R.id.ageEditText);
         acProvinceTextView = findViewById(R.id.provinceCompleteTextView);
         acCountyTextView = findViewById(R.id.countyAutoCompleteTextView);
         acCityTextView = findViewById(R.id.cityAutoCompleteTextView);
         acCityTextView.setVisibility(View.INVISIBLE);
         acCountyTextView.setVisibility(View.INVISIBLE);
         houseSizeEtext.requestFocus();

        SharedPreferences sharedPreferences = this.getSharedPreferences("logedin", MODE_PRIVATE);
        if (sharedPreferences.contains("username") && sharedPreferences.contains("password")){
            ownerId = sharedPreferences.getLong("id", 0);
        }else {
            Toast.makeText(this, "شما وارد سیستم نشده اید!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SignupActivity.class));
        }

      ImageButton mapButton = findViewById(R.id.addHouseLocationButton);
      mapButton.setOnClickListener((v) ->{
          Intent i = new Intent(getApplicationContext(),HouseLocationActivity.class );
          i.putExtra("intentSender", "register");
          startActivity(i);
      });
       Intent i = getIntent();

            System.out.println("Activity name: " + RegisterHouseActivity.class.getSimpleName());

        if (i.getParcelableExtra("location") != null){
          location = i.getParcelableExtra("location");
      }


        loadProvinces();


        acCityTextView.setOnItemClickListener((parent, view, position, id)->{
            int cityId = cityMap.get(acCityTextView.getText().toString());
            city = new City(cityId, acCityTextView.getText().toString());
        });
        acCountyTextView.setOnItemClickListener((parent, view, positon, id) ->{
            acCityTextView.setVisibility(View.VISIBLE);
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

        ArrayAdapter<String> sellRentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Arrays.asList("نوع درخواست", "فروش", "اجاره"));
        sellRentSpinner.setAdapter(sellRentAdapter);

        MySpinnerAdapter mySpinnerAdapter = new MySpinnerAdapter(this, SpinnerData.getInstance().getSpinnerItems(), SpinnerData.getInstance().getImages());
        houseTypeSpinner.setAdapter(mySpinnerAdapter);

        sellRentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              int reqPostition = position - 1;
               if (reqPostition >= 0) sellRent = SellRent.values()[reqPostition];
                if (sellRent == SellRent.RENT){
                    rentalCostEText.setEnabled(true);

                }else {
                    rentalCostEText.setEnabled(false);
                    rentalCostEText.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        houseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               int typePosition = position - 1;
               if (typePosition >= 0) houseType = HouseType.values()[typePosition];
                if (houseType == HouseType.SHOP || houseType == HouseType.APARTMENT ){
                    floorEtext.setEnabled(true);
                }else {
                    floorEtext.setEnabled(false);
                    floorEtext.setText("");
                }
                 ageEtext.setEnabled(true);
                if (houseType == HouseType.LAND){
                    ageEtext.setEnabled(false);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void loadProvinces() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mapProvince == null) {
                        mapProvince = ProvinceCityController.getProvinces();
                        List<String> list = new ArrayList<>();
                        list.addAll(mapProvince.keySet());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                provinceAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
                                acProvinceTextView.setAdapter(provinceAdapter);
                                provinceAdapter.notifyDataSetChanged();
                            }
                        });


                    }
                }  catch (ExecutionException | InterruptedException e1 ) {
                    showMessage("خطا در شبکه!");
                }
            }
        }).start();
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    private void cleaForm() {
        houseTypeSpinner.setSelection(0);
        sellRentSpinner.setSelection(0);
        floorEtext.setText("");
        houseSizeEtext.setText("");
        priceEtext.setText("");
        addressEtext.setText("");
        rentalCostEText.setText("");
        acProvinceTextView.setText("");
        acCountyTextView.setText("");
        acCityTextView.setText("");
        ageEtext.setText("");
        descriptionEtext.setText("");
        acCityTextView.setVisibility(View.INVISIBLE);
        acCountyTextView.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

}