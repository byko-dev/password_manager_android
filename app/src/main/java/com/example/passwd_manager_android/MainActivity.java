package com.example.passwd_manager_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passwd_manager_encryption.Aes256;


public class MainActivity extends AppCompatActivity implements TextWatcher, CompoundButton.OnCheckedChangeListener {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String KEY_PASS = "password";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_REMEMBER = "remember";
    private static final String PREF_NAME = "pref";
    private static CheckBox saveCre;
    private static EditText login;
    private static EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        login = findViewById(R.id.textLogin);
        password = findViewById(R.id.textPassword);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        saveCre = findViewById(R.id.checkBox2);

        if(sharedPreferences.getBoolean(KEY_REMEMBER, false))
            saveCre.setChecked(true);
        else
            saveCre.setChecked(false);

        login.setText(sharedPreferences.getString(KEY_USERNAME, ""));
        password.setText(sharedPreferences.getString(KEY_PASS, ""));

        login.addTextChangedListener(this);
        password.addTextChangedListener(this);
        saveCre.setOnCheckedChangeListener(this);
    }

    public void onClickLoginButton(View view) {
        //p1000 crash fix bro :D
        if (Build.VERSION.SDK_INT >= 24) { //9
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if(Mongo.login(login.getText().toString(), password.getText().toString(), view)){
            Intent intent = new Intent(MainActivity.this, TableActivity.class);
            startActivity(intent);
        }

        Aes256.setDefaultPassword(password.getText().toString());

    }

    public void hidePassword(View view){
        CheckBox checkbox = findViewById(R.id.checkBox);

        if(checkbox.isChecked()){
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else{
            password.setTransformationMethod(new PasswordTransformationMethod());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        managePrefs();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        managePrefs();
    }

    public void managePrefs(){
        if(saveCre.isChecked()){
            editor.putString(KEY_USERNAME, login.getText().toString().trim());
            editor.putString(KEY_PASS, password.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER, true);
            editor.apply();
        }else {
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_PASS);
            editor.remove(KEY_USERNAME);;
            editor.apply();
        }
    }


    public static void clear(){
        if(saveCre.isChecked() == false){
            login.setText("");
            password.setText("");
        }
    }


}
