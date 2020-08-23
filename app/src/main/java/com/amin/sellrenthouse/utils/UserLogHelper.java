package com.amin.sellrenthouse.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import com.amin.sellrenthouse.entities.User;

public class UserLogHelper extends AppCompatActivity {
    private  final SharedPreferences sharedPreferences;
    private  final  SharedPreferences.Editor spEditor;

    public UserLogHelper(Context context){
        sharedPreferences = context.getSharedPreferences("logedin", MODE_PRIVATE);
        spEditor = sharedPreferences.edit();
    }


    public  boolean logIn(User user){
        spEditor.putLong("id", user.getId());
        spEditor.putString("username", user.getUserName());
        spEditor.putString("password", user.getPassword());
        spEditor.putString("firstname", user.getFirstName());
        spEditor.putString("lastname", user.getLastName());
        spEditor.putString("contactnumber", user.getContactNumber());
        spEditor.commit();

        return true;
    }
    public User getCurrentUser(){
       User user = null;
        if (isLogedIn()){
            user = new User();
            user.setId(sharedPreferences.getLong("id", -1));
            user.setUserName(sharedPreferences.getString("username", ""));
            user.setPassword("");
            user.setFirstName(sharedPreferences.getString("firstname", ""));
            user.setLastName(sharedPreferences.getString("lastname", ""));
            user.setContactNumber(sharedPreferences.getString("contactnumber", ""));

            return user;
        }
        return user;
    }
    public void logout() {
        if (isLogedIn()) {
            spEditor.clear();
            spEditor.commit();
          }
    }
    public boolean isLogedIn(){
        return sharedPreferences.contains("username") && sharedPreferences.contains("password");

    }
}
