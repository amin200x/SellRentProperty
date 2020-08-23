package com.amin.sellrenthouse.launch;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.amin.sellrenthouse.R;
import com.amin.sellrenthouse.controller.UserController;
import com.amin.sellrenthouse.entities.User;
import com.amin.sellrenthouse.manager.UserManager;
import java.util.concurrent.ExecutionException;

public class SignupActivity extends AppCompatActivity {
    private EditText firstNameEtext;
    private EditText lastNameEtext;
    private EditText contactNumberEtext;
    private EditText userNameEtext;
    private EditText passwordEtext;
    private EditText rePasswordEtext;

    public void signUp(View view){
        String firstName = firstNameEtext.getText().toString();
        String lastName = lastNameEtext.getText().toString();
        String contactNumber = contactNumberEtext.getText().toString();
        String userName = userNameEtext.getText().toString().toLowerCase();
        String password = passwordEtext.getText().toString();
        String rePassword = rePasswordEtext.getText().toString();
        if(userName.isEmpty()){
            Toast.makeText(this, "نام کاربری نمی تواند خالی باشد!", Toast.LENGTH_LONG).show();
            return;

        }
        else if (password.isEmpty()) {
            Toast.makeText(this, "بخش رمز خالی است!!", Toast.LENGTH_LONG).show();
            return;

        }else if(!rePassword.equals(password)){
            Toast.makeText(this, "رمز  و تکرار رمز برابر نیستند!", Toast.LENGTH_LONG).show();
            return;

        }else if(password.length() < 6){
            Toast.makeText(this, "طول رمز باید حداقل 6 کاراکتر باشد!!", Toast.LENGTH_LONG).show();
            return;

        }else{
        User user = UserManager.getInstance().createUser(firstName, lastName, contactNumber,
                userName, password);
            String msg = "";
            try {
             Exception exception = UserController.getInstance().signUp(user);

             if (exception == null){
                 Toast.makeText(this, "ثبت با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
              Intent i = new Intent(this, SigninActivity.class);
              startActivity(i);
              this.finish();

             }else {
                    if (exception.getMessage().contains("contact_number") ){
                        Toast.makeText(this, "شماره تماس مورد نظر قبلا ثبت شده است!", Toast.LENGTH_SHORT).show();

                    }
                 else if (exception.getMessage().contains("user_name") ){
                     Toast.makeText(this, "نام کاربری مورد نظر قبلا ثبت شده است!", Toast.LENGTH_SHORT).show();

                 }else {
                     msg = "ثبت ناموفق";
                 }
             }
            } catch (ExecutionException |InterruptedException e ) {
                msg = "ثبت ناموفق";

            }finally {
                if (!msg.equals("")){
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

                }
            }



        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstNameEtext = findViewById(R.id.firstnameEditText);
        lastNameEtext = findViewById(R.id.lastnameEditText);
        contactNumberEtext = findViewById(R.id.contactNumberEditText);
        userNameEtext = findViewById(R.id.usernameEditText);
        passwordEtext = findViewById(R.id.passwordEditText);
        rePasswordEtext = findViewById(R.id.rePasswordEditText);
        showPrivacyDialog();

    }

    public void showPrivacyDialog(){
        AlertDialog.Builder aboutDialog = new  AlertDialog.Builder(this);
        TextView contentTextView = new TextView(this);
        ImageView imageView = new ImageView(this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        imageView.setImageResource(android.R.drawable.ic_dialog_info);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
        contentTextView.setTextSize( 18f);
        contentTextView.setGravity(Gravity.CENTER);
        contentTextView.setText(R.string.privacy);
        linearLayout.addView(imageView);
        linearLayout.addView(contentTextView);
        aboutDialog.setView(linearLayout)

                .setTitle("حریم خصوصی")
                .setCancelable(false)
                .setPositiveButton("تایید", (dialog,  which) -> dialog.dismiss()).show();


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
            startActivity(new Intent(this, SigninActivity.class));

        }
        return super.onKeyDown(keyCode, event);
    }
}
