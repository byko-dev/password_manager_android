package com.example.passwd_manager_android;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;

public class AddActivity extends AppCompatActivity {

    private final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private final String NUMBER = "0123456789";

    private final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private SecureRandom random = new SecureRandom();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EditText password = findViewById(R.id.editPassword);

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView counter = findViewById(R.id.textView14);
                EditText password = findViewById(R.id.editPassword);
                counter.setText("Chars [" + password.length() + "]");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }



    public void addNewPassword(View view){
        EditText title = findViewById(R.id.editTitle);
        EditText username = findViewById(R.id.editUsername);
        EditText password = findViewById(R.id.editPassword);
        EditText url = findViewById(R.id.editUrl);
        Mongo.addPassword(title.getText().toString(), username.getText().toString(), password.getText().toString(), url.getText().toString());
        super.onBackPressed();
    }


    public void generate(View view){
        EditText password = findViewById(R.id.editPassword);
        password.setText(generateRandomString(10));
    }

    public String generateRandomString(int length) {
        if (length < 1) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);

        }
        return sb.toString();
    }

}
