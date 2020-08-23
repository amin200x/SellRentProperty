package com.amin.sellrenthouse.launch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.amin.sellrenthouse.R;
import com.amin.sellrenthouse.controller.UserController;
import com.amin.sellrenthouse.entities.User;
import com.amin.sellrenthouse.utils.UserLogHelper;

import java.util.concurrent.ExecutionException;

public class UpdateProfileActivity extends AppCompatActivity {
    private EditText firstNameEtext;
    private  EditText lastNameEtext;
    private  EditText contactNumberEtext;
    private  EditText oldPasswordEtext;
    private EditText newPasswordEtext;
    private User currentUser = null;

    public void updateProfile(View view){
        String firstName = firstNameEtext.getText().toString();
        String lastName = lastNameEtext.getText().toString();
        String contactNumber = contactNumberEtext.getText().toString();
        String password = oldPasswordEtext.getText().toString();
        String rePassword = newPasswordEtext.getText().toString();

        if (!password.isEmpty()) {
            if (!rePassword.equals(password)) {
                Toast.makeText(this, "رمز  و تکرار رمز برابر نیستند!", Toast.LENGTH_LONG).show();
                return;

            } else if (password.length() < 6) {
                Toast.makeText(this, "طول رمز باید حداقل 6 کاراکتر باشد!!", Toast.LENGTH_LONG).show();
                return;

            }
        }
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setContactNumber(contactNumber);
       if (!password.isEmpty())
            currentUser.setPassword(password);

        String msg = "";
        try {
          Exception exception = UserController.getInstance().updateProfile(currentUser);

            if (exception == null){
                Toast.makeText(this, "ویرایش با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
               new UserLogHelper(this).logIn(currentUser);

            }else {
                if (exception.getMessage().contains("contact_number") ){
                    Toast.makeText(this, "شماره تماس مورد نظر قبلا ثبت شده است!!", Toast.LENGTH_SHORT).show();

                }
               else {
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

        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        firstNameEtext = findViewById(R.id.firstnameEditText);
        lastNameEtext = findViewById(R.id.lastnameEditText);
        contactNumberEtext = findViewById(R.id.contactNumberEditText);
        EditText userNameEtext = findViewById(R.id.usernameEditText);
        oldPasswordEtext = findViewById(R.id.passwordEditText);
        newPasswordEtext = findViewById(R.id.rePasswordEditText);
        userNameEtext.setEnabled(false);

        currentUser = new UserLogHelper(this).getCurrentUser();
        if ( currentUser != null){
            firstNameEtext.setText(currentUser.getFirstName());
            lastNameEtext.setText(currentUser.getLastName());
            contactNumberEtext.setText(currentUser.getContactNumber());
            userNameEtext.setText(currentUser.getUserName());

        }else {
            startActivity(new Intent(this, SigninActivity.class));
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return super.onKeyDown(keyCode, event);

    }
}
