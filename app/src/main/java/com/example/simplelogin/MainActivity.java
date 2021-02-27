/*
Davidso,s
Antonio Sanchez
Moayad Badawi
CECS 453

Assignement #2
 */

package com.example.simplelogin;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

// These library is added by Android Studeio
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static public HashMap<String,String> userLogins = new HashMap<>();

    // Member variables for controls
    private Button m_btnLogin = null;
    private EditText m_txtPassword = null;
    private Button m_btnSignUp = null;
    private EditText m_txtUsername=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userLogins.put("moebad38","hello");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating control objects
        m_btnLogin = findViewById(R.id.btnLogin);
        m_txtPassword = findViewById(R.id.txtPassword);
        m_btnSignUp = findViewById(R.id.signUP_button);
        m_txtUsername = findViewById(R.id.txtUsername);

        m_btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String password = "";
                String username = "";

                //Reading the password control
                password = m_txtPassword.getText().toString();
                username = m_txtUsername.getText().toString();

                // Some validation ...
                if (userLogins.containsKey(username) && userLogins.get(username).equals(password)) {
                    // Sending a string to the welcome window.
                    Intent intent = new Intent(getApplicationContext(), NavigationDrawer.class); //check if the username exist  and the password matches to that user  name.
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect Log-In Info!", Toast.LENGTH_LONG).show();
                }
            }
        });
        m_btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUp();
            }
        });
    }
        public void openSignUp(){
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        }

    }

