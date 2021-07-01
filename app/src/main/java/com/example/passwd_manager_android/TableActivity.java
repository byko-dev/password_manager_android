package com.example.passwd_manager_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.passwd_manager_encryption.Aes256;

import org.bson.Document;

import java.util.ArrayList;


public class TableActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayAdapter<String> adapter;
    private Thread threadRefresh;
    private ArrayList<String> titles = new ArrayList<String>();
    private ArrayList<String> _id = new ArrayList<String>();
    private ArrayList<String> usernameList = new ArrayList<String>();
    private ArrayList<String> passwordList = new ArrayList<String>();
    private ArrayList<String> dateList = new ArrayList<String>();
    private ArrayList<String> urlList = new ArrayList<String>();

    private static boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button addPswButton = new Button(this);
        addPswButton.setText("Add");
        Toolbar.LayoutParams lay = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,Toolbar.LayoutParams.WRAP_CONTENT);
        lay.gravity = Gravity.END;
        addPswButton.setLayoutParams(lay);
        toolbar.addView(addPswButton);
        addPswButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
        //logout button
        Button backButton = new Button(this);
        backButton.setText("log out");
        Toolbar.LayoutParams layL = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,Toolbar.LayoutParams.WRAP_CONTENT);
        layL.gravity = Gravity.RIGHT;
        backButton.setLayoutParams(layL);
        toolbar.addView(backButton);
        backButton.setOnClickListener(this);

        final ListView listView1 = (ListView) findViewById(R.id.listView);
        ArrayList<Document> docs = Mongo.getAllPsw();

        adapter = new ArrayAdapter<String>(this, R.layout.text_view, titles);

        listView1.setAdapter(adapter);

        running = true;
        refresh();

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(TableActivity.this, InfoActivity.class);
                //przekazywanie argumentow do inetnetu
                intent.putExtra("title", titles.get(position));
                intent.putExtra("usr", usernameList.get(position));
                intent.putExtra("psw", passwordList.get(position));
                intent.putExtra("date", dateList.get(position));
                intent.putExtra("url", urlList.get(position));
                intent.putExtra("_id", _id.get(position));

                startActivity(intent);
            }
        });
    }

    public void refresh(){

        threadRefresh = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    if(!running){ //jest git
                        try {
                            threadRefresh.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                        //download all documents from database
                        ArrayList<Document> docs = Mongo.getAllPsw();
                        final ArrayList<String> new_titles = new ArrayList<String>();
                        final ArrayList<String> new_usernameList = new ArrayList<String>();
                        final ArrayList<String> new_passwordList = new ArrayList<String>();
                        final ArrayList<String> new_urlList = new ArrayList<String>();
                        final ArrayList<String> new_id = new ArrayList<String>();
                        final ArrayList<String> new_dateList = new ArrayList<String>();

                        for (Document doc : docs) {
                            try {
                                new_titles.add(Aes256.decrypt(doc.get("title").toString()));
                                new_usernameList.add(Aes256.decrypt(doc.get("usr").toString()));
                                new_passwordList.add(Aes256.decrypt(doc.get("psw").toString()));
                                new_urlList.add(Aes256.decrypt(doc.get("url").toString()));
                                new_dateList.add(Aes256.decrypt(doc.get("date").toString()));
                                new_id.add(doc.get("_id").toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (new_titles.equals(titles) && new_usernameList.equals(usernameList) &&
                                new_passwordList.equals(passwordList) && new_urlList.equals(urlList)) {
                            try {
                                threadRefresh.sleep(500); //do nothing
                                //
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            titles = new_titles;
                            usernameList = new_usernameList;
                            passwordList = new_passwordList;
                            urlList = new_urlList;
                            _id = new_id;
                            dateList = new_dateList;

                            TableActivity.this.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    adapter.clear();
                                    adapter.addAll(titles);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }
        });
        threadRefresh.start();
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.clear();
                        TableActivity.super.onBackPressed();
                        running = false;
                    }
                }).create().show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)   {

            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            MainActivity.clear();
                            finish();
                            running = false;
                        }
                    }).create().show();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        TableActivity.this.onBackPressed();
    }
}
