package com.example.passwd_manager_encryption;
import android.os.Build;

import java.util.Base64;
import static java.nio.charset.StandardCharsets.UTF_8;


public class Aes256 extends AbstractAes256
{

    private static String passwd;
    public static void setDefaultPassword(String password){
        passwd = password;
    }

    public static String encrypt(String input) throws Exception {
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            return Base64.getEncoder().encodeToString(_encrypt(input.getBytes(UTF_8), passwd.getBytes(UTF_8)));
        else{
            return new String(android.util.Base64.encode(_encrypt(input.getBytes(UTF_8), passwd.getBytes(UTF_8)), android.util.Base64.DEFAULT));
        }
    }

    public static String decrypt(String crypted) throws Exception {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new String(_decrypt(Base64.getDecoder().decode(crypted), passwd.getBytes(UTF_8)), UTF_8);
        }
        else{
            return new String(_decrypt(android.util.Base64.decode(crypted, android.util.Base64.DEFAULT), passwd.getBytes()), UTF_8);
        }
    }
}
