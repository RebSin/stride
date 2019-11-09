package com.example.stride;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

    TextView usernameTextView, passwordTextView;
    EditText userInput, passInput;

    public static TextView EditContent;

    public static final String DEFAULT = "not available";
    public static final String DEFAULT2 = "not available";


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameTextView = (TextView)findViewById(R.id.retrUsername);
        passwordTextView = (TextView)findViewById(R.id.retrPassword);

        userInput = (EditText)findViewById(R.id.userInput);
        passInput = (EditText)findViewById(R.id.passInput);

        EditContent = (TextView) findViewById(R.id.EditContent);

        getSharedPreferences("size", Context.MODE_PRIVATE);
        getSharedPreferences("color", Context.MODE_PRIVATE);
        getSharedPreferences("bg", Context.MODE_PRIVATE);
    }

    public void toSettings(View view){
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String username = sharedPrefs.getString("username", DEFAULT);
        String password = sharedPrefs.getString("password", DEFAULT);

        String user = userInput.getText().toString();
        String pass = passInput.getText().toString();

        if (username.equals(user) && password.equals(pass)) {
            userInput.setText(username);
            passInput.setText(password);
            Toast.makeText(this, "Logging In", Toast.LENGTH_LONG).show();
            Intent intent= new Intent(this, MapsActivity.class);
            startActivity(intent);

        } else {
            //if they put in the wrong username and password, make then sign up again
            Toast.makeText(this, "Wrong Username or Password", Toast.LENGTH_LONG).show();
            SharedPreferences preferences = getSharedPreferences("MyData", 0);
            preferences.edit().remove("username").commit();
            preferences.edit().remove("password").commit();

            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);

        }
    }
}