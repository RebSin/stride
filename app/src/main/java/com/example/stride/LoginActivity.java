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
    TextView usernameTextView, passwordTextView; //declare textView variables
    EditText userInput, passInput; //devlare edittext variables
    public static TextView EditContent;

    //used to check data in shared preferences
    public static final String DEFAULT = "not available";
    public static final String DEFAULT2 = "not available";


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //link textViews to XML file
        usernameTextView = (TextView)findViewById(R.id.retrUsername);
        passwordTextView = (TextView)findViewById(R.id.retrPassword);
        EditContent = (TextView) findViewById(R.id.EditContent);

        //link EditTexts to XML file
        userInput = (EditText)findViewById(R.id.userInput);
        passInput = (EditText)findViewById(R.id.passInput);
    }

    public void toSettings(View view){
        //use shared preferences to get the usernmae and password that the user had entered on the register activity
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String username = sharedPrefs.getString("username", DEFAULT);
        String password = sharedPrefs.getString("password", DEFAULT);

        //get the username and password the user entered into the login activity
        String user = userInput.getText().toString();
        String pass = passInput.getText().toString();

        //check to see if the username and password from shared preferences matches the one entered in the login activity
        if (username.equals(user) && password.equals(pass)) {
            userInput.setText(username);
            passInput.setText(password);
            //if it matches, display a toast message
            Toast.makeText(this, "Logging In", Toast.LENGTH_LONG).show();
            //go to the main activity of the application
            Intent intent= new Intent(this, MapsActivity.class);
            startActivity(intent);

        } else {
            //if they put in the wrong username and password, make them sign up again
            Toast.makeText(this, "Wrong Username or Password", Toast.LENGTH_LONG).show();
            SharedPreferences preferences = getSharedPreferences("MyData", 0);
            //remove the current username and password stored in shared preferences
            preferences.edit().remove("username").commit();
            preferences.edit().remove("password").commit();

            //start the register activity
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);

        }
    }
}