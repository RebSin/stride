package com.example.stride;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends Activity {
    EditText usernameEditText, passwordEditText;
    public static final String DEFAULT = "not available";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //link the username and password to the XML
        usernameEditText = (EditText)findViewById(R.id.editTextUsername);
        passwordEditText = (EditText)findViewById(R.id.editTextPassword);

        //check if something in sharedPref - its not the first time
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", DEFAULT);
        String password = sharedPreferences.getString("password", DEFAULT);

        //if exists go to the main activity (currently the maps activity)--temporarily going to the login activity
        if (!username.equals(DEFAULT) && !password.equals(DEFAULT)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void gotoActivity2(View view){
        //saved data in sharedpref
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        //put the username and password into shared preferences
        editor.putString("username", usernameEditText.getText().toString());
        editor.putString("password", passwordEditText.getText().toString());
        editor.commit();

        //add a toast message to tell the user their account has successfully been made
        Toast.makeText(this, "Account Made", Toast.LENGTH_LONG).show();

        //start the login activity through an intent
        Intent intent= new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
