/*
Antonio Sanchez
Moayad Badawi
CECS 453
Assignement #1
Due Date 2/20/2020
 */
package com.example.simplelogin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondClass extends AppCompatActivity {

    // Member variable for holding a textview object
    private TextView m_txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondclass);

        // Textview object assignment
        m_txtMessage = findViewById(R.id.txtMessage);

        // Reading the value of the intent sent here
        Intent intent = getIntent();
        String message = intent.getStringExtra("FirstMessage");
        // Assigning the value to the TextView object
        m_txtMessage.setText(message);

    }

    public static class CarShow {
    }
}
