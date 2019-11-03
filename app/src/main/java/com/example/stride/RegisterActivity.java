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
        usernameEditText = (EditText)findViewById(R.id.editTextUsername);
        passwordEditText = (EditText)findViewById(R.id.editTextPassword);

        //check if something in sharedPref - its not the first time
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", DEFAULT);
        String password = sharedPreferences.getString("password", DEFAULT);

        //if exists go to the main activity (currently the maps activity)
        if (!username.equals(DEFAULT) && !password.equals(DEFAULT)) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
    }

    public void gotoActivity2(View view){
        //saved data in sharedpref
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("username", usernameEditText.getText().toString());
        editor.putString("password", passwordEditText.getText().toString());
        editor.commit();

        Toast.makeText(this, "Account Made", Toast.LENGTH_LONG).show();
        Intent intent= new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}
