package com.amin.sellrenthouse.launch;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amin.sellrenthouse.R;
import com.amin.sellrenthouse.controller.UserController;
import com.amin.sellrenthouse.dao.HouseDao;
import com.amin.sellrenthouse.entities.House;
import com.amin.sellrenthouse.entities.User;
import com.amin.sellrenthouse.utils.UserLogHelper;

import java.util.concurrent.ExecutionException;

public class HouseDetailActivity extends AppCompatActivity {
     private  Location mLocation;
     private  Location houseLocation;
     private  House house;

     public void showImages(View  view){
            Intent intent = new Intent(this, HouseImagesActivity.class);
            if (house == null) return;
            intent.putExtra("houseId", house.getId());
            startActivity(intent);
    }
    public void showOnMap(View view){
        if (houseLocation.getLongitude() != 0 || houseLocation.getLatitude() != 0){
            Intent intent = new Intent(getApplicationContext(), HouseOnMapActivity.class);
            intent.putExtra("mLocation", mLocation);
            intent.putExtra("hLocation", houseLocation);
            startActivity(intent);
        }
    }

    public void report(View view){
    AlertDialog.Builder dialog =    new AlertDialog.Builder(HouseDetailActivity.this)
                .setTitle(" گزارش محتوای نامناسب")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("مایل به گزارش این آگهی هستید؟")
                .setCancelable(false)
                .setPositiveButton("تایید", (d, w)-> {
                    HouseDao dao = new HouseDao();
                    house.setReported(true);

                        new Thread(() ->{
                            try {
                                dao.report(house);
                                Toast.makeText(HouseDetailActivity.this, "گزارش با موفقیت ثبت شد!", Toast.LENGTH_SHORT).show();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }).start();

                } )
                .setNegativeButton("انصراف", (d, w) -> {
                    d.dismiss();
                });
        dialog.create().show();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detailctivity);
        TextView detailEditText = findViewById(R.id.detailTextView);
        Button onMapButton = findViewById(R.id.onMapButton);
        onMapButton.setEnabled(false);
        house = this.getIntent().getParcelableExtra("house");

        mLocation = this.getIntent().getParcelableExtra("mLocation");
        if (house.getLocation() != null)
         houseLocation = house.getLocation();
        if (houseLocation != null && (houseLocation.getLongitude() != 0 && houseLocation.getLatitude() != 0))
            onMapButton.setEnabled(true);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(house);
        stringBuilder.append("-").append(house.getAddress()).append("\n");

        if (!house.getDescription().isEmpty())
            stringBuilder.append("توضیحات: ").append(house.getDescription()).append("\n \n");

        User user = new User();
        user.setId(house.getOwnerId());

            new Thread(new Runnable() {
                @Override
                public void run() {
                   final User owner;
                    try {
                        owner = UserController.getInstance().getUser(user).get(0);
                        stringBuilder.append("شماره تماس:   ").append(owner.getContactNumber()).append( " --- ").append(owner.getLastName());

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.progressBarHouseDetail).setVisibility(View.GONE);
                            detailEditText.setText(stringBuilder.toString());
                        }
                    });
                }
            }).start();



    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
