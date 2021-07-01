package com.example.passwd_manager_android;

import android.view.View;
import android.widget.Toast;

import com.example.passwd_manager_encryption.Aes256;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

public class Mongo {

    private static String username;
    private static MongoDatabase database;

    public static boolean login(String login, String password, View view){
        username = login;
        try{
            MongoClientURI uri = new MongoClientURI(
                    "mongodb://" +login + ":" + password /* deleted for security reasons */);

            MongoClient mongoClient = new MongoClient(uri);
            database = mongoClient.getDatabase("main_passwd_mgr");
            MongoCollection<Document> coll = database.getCollection("pwd_"+login);
        }catch(Exception ex){
            Toast.makeText(view.getContext(), "Password or login is wrong!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private static MongoCollection<Document> getUserCollection(){
        if(username.equals("root")){
            return database.getCollection("passwords");
        }else{
            return database.getCollection("pwd_" + username);
        }
    }

    public static ArrayList<Document> getAllPsw() {
       return getUserCollection().find(new Document()).into(new ArrayList<Document>());
    }

    public static void removeDocumnet(String id){
        getUserCollection().deleteOne(new Document("_id", new ObjectId(id)));
    }

    public static void updateDoc(String id,String titleStr, String urlStr, String usrStr, String pswStr){
        Document doc = new Document();
        try {
            doc.append("title", Aes256.encrypt(titleStr))
                    .append("url", Aes256.encrypt(urlStr))
                    .append("usr", Aes256.encrypt(usrStr))
                    .append("psw", Aes256.encrypt(pswStr))
                    .append("date", Aes256.encrypt(getCurrentDate()));
        } catch (Exception e) {
            e.printStackTrace();
        }

         getUserCollection().replaceOne(new Document("_id", new ObjectId(id)), doc);
    }


    public static void addPassword(String titleStr, String usrStr, String pswStr, String urlStr) {
        MongoCollection<Document> coll = getUserCollection();

        Document doc = new Document("_id", new ObjectId());
        try {
            doc.append("title", Aes256.encrypt(titleStr))
                    .append("url", Aes256.encrypt(urlStr))
                    .append("usr", Aes256.encrypt(usrStr))
                    .append("psw", Aes256.encrypt(pswStr))
                    .append("date", Aes256.encrypt(getCurrentDate()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        coll.insertOne(doc);
    }

    private static String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return sdf.format(new Date());
    }
}
