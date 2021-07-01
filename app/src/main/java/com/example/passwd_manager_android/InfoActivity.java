package com.example.passwd_manager_android;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends AppCompatActivity {

    private TableActivity table;
    private TextView titleTV;
    private EditText usernameET;
    private EditText passwordET;
    private EditText urlET;
    private TextView date;
    private String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleTV = (TextView)findViewById(R.id.editTitle);
        usernameET = (EditText) findViewById(R.id.editUsername);
        passwordET =(EditText) findViewById(R.id.editPassword);
        urlET = (EditText) findViewById(R.id.editUrl);

        Intent myIntent = getIntent(); //get this intent with args
        date = (TextView) findViewById(R.id.textView7);
        titleTV.setText(myIntent.getStringExtra("title"));
        usernameET.setText(myIntent.getStringExtra("usr"));
        passwordET.setText(myIntent.getStringExtra("psw"));
        urlET.setText(myIntent.getStringExtra("url"));
        date.setText(myIntent.getStringExtra("date"));
        _id = myIntent.getStringExtra("_id");
        setTitle(myIntent.getStringExtra("title"));
    }

    public void  deleteDocumentBtn_OnClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setCancelable(true);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete changes?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Mongo.removeDocumnet(_id);
                        Toast.makeText(getApplicationContext(), "Deleted " + titleTV.getText().toString(), Toast.LENGTH_SHORT).show();
                        InfoActivity.this.onBackPressed();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int witch)/*Witch xd WiedzmaXDDD */ {
                //Do nothing
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void editDocumentBtn_OnClick(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setCancelable(true);
        builder.setTitle("Edit");
        builder.setMessage("Are you sure you want to save changes?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Mongo.updateDoc(_id, titleTV.getText().toString(), urlET.getText().toString(), usernameET.getText().toString(), passwordET.getText().toString());
                        Toast.makeText(getApplicationContext(), "Changes saved!", Toast.LENGTH_SHORT).show();
                        setTitle(titleTV.getText().toString());
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    public void copyUsrBtn_Click(View view){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("username",usernameET.getText() );
        clipboard.setPrimaryClip(clip);

        Toast.makeText(getApplicationContext(), "Username copied!", Toast.LENGTH_SHORT).show();
    }

    public void copyPasswordBtn_Click(View view){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("password",passwordET.getText() );
        clipboard.setPrimaryClip(clip);

        Toast.makeText(getApplicationContext(), "Password copied!", Toast.LENGTH_SHORT).show();
    }


}
