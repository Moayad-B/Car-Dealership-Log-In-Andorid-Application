/*
Antonio Sanchez
Moayad Badawi
CECS 453
Assignement #1
Due Date 2/20/2020
 */

package com.example.simplelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private Button s_buttonRegister = null;
    private EditText s_passwordTxt1 = null; //declare the  variables
    private EditText s_passwordTxt2 = null;
    private EditText s_userName = null;
    private EditText s_email = null;
    private EditText s_phone = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        s_buttonRegister = findViewById(R.id.register_btn);
        s_passwordTxt1 = findViewById(R.id.password1_txt); //get all the objects from  the view/activity
        s_passwordTxt2 = findViewById(R.id.password2_txt);
        s_userName = findViewById(R.id.userName_txt);
        s_email = findViewById(R.id.email_txt);
        s_phone = findViewById(R.id.phone_txt);

        s_buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                //check all the parameters in the 2nd activity requried in order to sign up
                String passwordTxt1 = "";
                String passwordTxt2 = "";
                String userName = "";
                String email = "";
                String phone = "";

                passwordTxt1 = s_passwordTxt1.getText().toString();
                passwordTxt2 = s_passwordTxt2.getText().toString();
                userName = s_userName.getText().toString();
                email = s_email.getText().toString().trim();
                phone = s_phone.getText().toString();
                // input  validation for everything
                if(TextUtils.isEmpty(userName))
                    Toast.makeText(getApplicationContext(), "User Name Cannot be Empty!", Toast.LENGTH_LONG).show();
                else if(TextUtils.isEmpty(email) || !(Patterns.EMAIL_ADDRESS.matcher(email).matches()))
                    Toast.makeText(getApplicationContext(), "Incorrect Email Format!", Toast.LENGTH_LONG).show();
                else if(!passwordTxt1.equals(passwordTxt2))
                    Toast.makeText(getApplicationContext(), "Passwords Do Not Match!", Toast.LENGTH_LONG).show();
                else if(TextUtils.isEmpty(phone)  || !(Patterns.PHONE.matcher(phone).matches()))
                    Toast.makeText(getApplicationContext(), "Incorrect Phone Format!", Toast.LENGTH_LONG).show();
                else if(MainActivity.userLogins.containsKey(userName))
                    Toast.makeText(getApplicationContext(), "User Name Is Already Taken!", Toast.LENGTH_LONG).show();
                else {
                    MainActivity.userLogins.put(userName,passwordTxt1);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
