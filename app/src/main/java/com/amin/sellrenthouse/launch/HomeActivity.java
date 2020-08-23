package com.amin.sellrenthouse.launch;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.amin.sellrenthouse.R;
import com.amin.sellrenthouse.controller.UserController;
import com.amin.sellrenthouse.entities.User;
import com.amin.sellrenthouse.utils.UserLogHelper;


import java.util.concurrent.ExecutionException;

public class HomeActivity extends AppCompatActivity {
    ImageButton addHouseImageButton;
    ImageButton editHouseImageButton;
    private  User user = null;

    public void showAddHouse(View view){
        Intent i = new Intent(getApplicationContext(), RegisterHouseActivity.class);
        startActivity(i);
    }
    public void updateProfile(View view){
        Intent i = new Intent(getApplicationContext(), UpdateProfileActivity.class);
        startActivity(i);
    }
    public void removeAll(View view){
        deleteAll();
          }
    public void myHouseList(View view){
        Intent i = new Intent(getApplicationContext(), MyHouseActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addHouseImageButton = findViewById(R.id.addHouseImageButton);
        editHouseImageButton =findViewById(R.id.editHouseImageButton);

        if (!new UserLogHelper(this).isLogedIn()){
            startActivity(new Intent(getApplicationContext(), SigninActivity.class));
        }else {
            user = new UserLogHelper(this).getCurrentUser();
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_main, menu);

        if (new UserLogHelper(this).isLogedIn()){
            MenuItem item = menu.findItem(R.id.login);
            item.setVisible(false);
            item = menu.findItem(R.id.logout);
            item.setVisible(true);
            item = menu.findItem(R.id.home);
            item.setVisible(true);
        }else {
            MenuItem item = menu.findItem(R.id.login);
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
                this.finish();
                intent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(intent);

                return true;
            case R.id.logout:
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.logout_icon)
                        .setMessage("از حساب کاربری خارج می شوید؟")
                        .setPositiveButton("بله", (d, w) -> {
                           new  UserLogHelper(HomeActivity.this).logout();
                            goSearchActivity();

                        })
                        .setNegativeButton("خیر", (d, v) -> {

                        }).show();
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

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void goSearchActivity(){
    Intent intent = new Intent(getApplicationContext(), SearchHouseActivity.class);
    startActivity(intent);
    this.finish();

    }

    public  void deleteAll( ){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("حذف حساب کاربری")
                .setMessage("با حذف این حساب کاربری کل اطلاعات ثبت شده توسط این حساب حذف خواهد شد!")
                .setIcon(R.drawable.delete_icon)
                .setPositiveButton("انجام عملیات حذف", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new AlertDialog.Builder(HomeActivity.this)
                                .setTitle("حذف")
                                .setIcon(android.R.drawable.ic_delete)
                                .setMessage("آیا مایل به حذف اطلاعات می باشید؟")
                                .setPositiveButton("بله", (d, w)-> {

                                       String msg = "";
                                        if (new  UserLogHelper(HomeActivity.this).isLogedIn()) {
                                            try {
                                                Exception exception =  UserController.getInstance().removeUser(user);
                                                if (exception == null){
                                                    msg = "حذف با موفقیت انجام شد!";
                                                    new  UserLogHelper(HomeActivity.this).logout();
                                                    goSearchActivity();
                                                }else {
                                                    msg = "حذف ناموفق دوباره تلاش کن!";
                                                    exception.printStackTrace();
                                                }
                                            } catch (ExecutionException | InterruptedException e) {
                                                e.printStackTrace();
                                                msg = "حذف ناموفق دوباره تلاش کن!";
                                            }finally {
                                                if (!msg.equals("")){
                                                    Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                }
                                            }


                                        }else{
                                            Toast.makeText(HomeActivity.this, "شما وارد سیستم نشده اید!", Toast.LENGTH_SHORT).show();
                                        }

                                })
                                .setNegativeButton("خیر", (d, w) -> {

                                }).show();
                    }
                })
                .setNegativeButton("لغو", (d, w) ->  {
                        return;
                    });
        dialog.create().show();



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
          this.finish();
          startActivity(new Intent(this, SearchHouseActivity.class));
          return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
