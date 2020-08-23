package com.amin.sellrenthouse.launch;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.amin.sellrenthouse.R;
import com.amin.sellrenthouse.controller.HouseController;
import com.amin.sellrenthouse.entities.House;
import com.amin.sellrenthouse.utils.CustomAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyHouseActivity extends AppCompatActivity {
    private List<House> myhouseList;
    private ListView lstView;
    private CustomAdapter customAdapter;
    private SharedPreferences sharedPreferences;
    private Thread getTask;
    private AlertDialog alert;
    private class GetData implements Runnable{
        private long userId;

        @Override
        public void run() {
            {
               getUserId();

                try {
                    myhouseList = HouseController.getInstance().getMyHouses(userId);
                } catch (ExecutionException e) {
                    runOnUiThread(()->{
                        showMessage("خطا در شبکه!");
                    });

                }catch(InterruptedException e){

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.progressBarMyHouses).setVisibility(View.GONE);
                    }
                });

                  checkListIfEmpty();

            }

        }
        private void getUserId(){

            if (isLogedIn()){
               userId = sharedPreferences.getLong("id", -1);
            }else{
                Toast.makeText(MyHouseActivity.this, "شما وارد سیستم نشده اید!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(i);

            }
        }
        private void checkListIfEmpty(){

            if (myhouseList.size() == 0){
               runOnUiThread(() -> {
                   {
                     alert =   new AlertDialog.Builder(MyHouseActivity.this)
                               .setMessage("شما هنوز درخواستی ثبت نکرده اید!")
                               .setPositiveButton("بازگشت", (dialog, which) -> {
                                         {
                                       startActivity(new Intent(MyHouseActivity.this, HomeActivity.class));
                                        }
                               }).show();
                   }
               });
        }else {
                runOnUiThread(() -> {

                        customAdapter = new CustomAdapter(MyHouseActivity.this, myhouseList);
                        lstView.setAdapter(customAdapter);
                        customAdapter.notifyDataSetChanged();

                });
            }
    }
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean isLogedIn() {
        sharedPreferences = getSharedPreferences("logedin", MODE_PRIVATE);
        return sharedPreferences.contains("username") && sharedPreferences.contains("password");

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_house);
        myhouseList = new ArrayList<>();
        lstView = findViewById(R.id.listView);
        findViewById(R.id.progressBarMyHouses).setVisibility(View.VISIBLE);

        getTask = new Thread(new GetData());
        getTask.start();

       lstView.setOnItemClickListener((parent,  view,   position,  id) -> {
             {
               new AlertDialog.Builder(MyHouseActivity.this)
                       .setTitle("ویرایش یا حذف")
                       .setIcon(android.R.drawable.ic_dialog_alert)
                       .setMessage("ویرایش و حذف اطلاعات! ")
                        .setPositiveButton("ویرایش گزینه انتخابی", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               Intent i = new Intent(getApplicationContext(), UpdateHouseActivity.class);
                               i.putExtra("house", myhouseList.get(position));
                               startActivity(i);
                               MyHouseActivity.this.finish();
                           }
                       })
                       .setNegativeButton("حذف گزینه انتخابی", (dialog, which) -> {
                            {
                               new AlertDialog.Builder(MyHouseActivity.this)
                                       .setTitle("حذف")
                                       .setMessage("گزینه انتخابی برای حذف")
                                       .setPositiveButton("حذف شود", (dialog1,  which1) -> {
                                            {
                                               try {
                                                   Exception exception = HouseController.getInstance().removeHouse(myhouseList.get(position));
                                                   if (exception == null){
                                                       showMessage("با موفقیت حذف شد!");
                                                       getTask = new Thread(new GetData());
                                                       getTask.start();
                                                   }else {
                                                       showMessage("خطا در شبکه!");
                                                   }
                                               } catch (ExecutionException |InterruptedException  e) {
                                                   showMessage("خطا در شبکه!");
                                               }
                                           }
                                       })
                                       .setNegativeButton("انصراف از حذف", (dialog1,  which1) -> {
                                           {
                                               dialog1.dismiss();
                                           }
                                       }).show();


                           }
                       }).setNeutralButton("لغو", (dialog,  which) -> {
                          {
                                dialog.dismiss();
                           }
               }).show();
           }
       });


    }

    @Override
    public void onBackPressed() {
        if (getTask.isAlive())
            getTask.interrupt();

        this.finish();
        super.onBackPressed();
    }


}
