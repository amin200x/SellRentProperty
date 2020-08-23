package com.amin.sellrenthouse.launch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.amin.sellrenthouse.R;
import com.amin.sellrenthouse.controller.UserController;
import com.amin.sellrenthouse.entities.User;
import com.amin.sellrenthouse.utils.UserLogHelper;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class SigninActivity extends AppCompatActivity {
    private EditText usernameEtext;
    private EditText passwordEtext;
    Button signinBtn;
    TextView signupWindoTextview;

    public void signIn(View view){
        String userName = usernameEtext.getText().toString();
        String password = passwordEtext.getText().toString();
        String msg = "";
        try {
            User user = new User();
            user.setUserName(userName.trim().toLowerCase());
            user.setPassword(password.trim().toLowerCase());
           List<User> list = UserController.getInstance().signIn(user);
            //sharedPreferences = getSharedPreferences("logedin", MODE_PRIVATE);
           if (list.size() == 1){
                //SharedPreferences.Editor spEditor = sharedPreferences.edit();
                User oUser = list.get(0);
               new UserLogHelper(this).logIn(oUser);
                goMainActivity();

            }else {
                msg = "نام کاربری یا رمز ورود اشتباه است!";
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            msg = "خطا در شبکه";
        }finally {
            if (!msg.equals("")){
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void goSignUpActivity(View view){
        this.finish();
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);

    }
    public void goMainActivity(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        usernameEtext = findViewById(R.id.usernameEditText);
        passwordEtext = findViewById(R.id.passwordEditText);
        signinBtn = findViewById(R.id.loginButton);
        signupWindoTextview = findViewById(R.id.signupWindowtextView);

        if (new UserLogHelper(this).isLogedIn())
            goMainActivity();



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
            startActivity(new Intent(this, SearchHouseActivity.class));
        }
        return true;

    }

}
