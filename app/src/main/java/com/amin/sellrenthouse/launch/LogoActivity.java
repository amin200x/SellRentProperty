package com.amin.sellrenthouse.launch;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.amin.sellrenthouse.R;

import java.util.concurrent.TimeUnit;

public class LogoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        startApp();
    }

    private void startApp() {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(6);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                runOnUiThread(() -> goNetDialogOrSearchActivity());
                }

        }) .start();
    }

    private void goNetDialogOrSearchActivity() {
        if (!isNetworkConnected()) {
            showNetworkDialog();
        } else {
               this.finish();
                startActivity(new Intent(getApplicationContext(), SearchHouseActivity.class));

        }
    }

    private boolean isNetworkConnected() {
       ConnectivityManager connectivityManager  = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void showNetworkDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LogoActivity.this);
        alertDialog.setTitle("اتصال شبکه")
                .setIcon(R.drawable.internet_connection)
                .setMessage("برای استفاده از نرم افزار نیاز هست که به اینترنت متصل باشید!")
                .setCancelable(false)
                .setPositiveButton("تنضیمات اتصال", (dialog,  which) -> {
                    {

                        Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivityForResult(i, 1);
                    }
                }).setNegativeButton("خروج", ((dialog, which) -> {
                       LogoActivity.this.finish();
                       System.exit(0);
        })) .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED){
            goNetDialogOrSearchActivity();
        }
    }
}
