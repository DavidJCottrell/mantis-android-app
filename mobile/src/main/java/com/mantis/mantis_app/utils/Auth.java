package com.mantis.mantis_app.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.mantis.mantis_app.app.dashboard.MainActivity;
import com.mantis.mantis_app.app.login.LoginActivity;

public class Auth {
    public static boolean isLoggedIn = false;

    // Get users stored auth token
    public static String getStoredToken(AppCompatActivity currentContext){
        SharedPreferences prefs = currentContext.getSharedPreferences("mantis_preferences", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        // Log the user out if they do not have a token
        if(token.equals("")) logout(currentContext);
        else isLoggedIn = true;

        return token;
    }

    public static void logout(AppCompatActivity currentContext){
        isLoggedIn = false;
        SharedPreferences.Editor editor = currentContext.getSharedPreferences("mantis_preferences", Context.MODE_PRIVATE).edit();
        editor.putString("token", "");
        editor.apply();
        // Take user to login page
        currentContext.startActivity(new Intent(currentContext, LoginActivity.class));
    }

    public static void login(AppCompatActivity currentContext, String token){
        isLoggedIn = true;
        SharedPreferences.Editor editor = currentContext.getSharedPreferences("mantis_preferences", Context.MODE_PRIVATE).edit();
        editor.putString("token", token);
        editor.apply();
        currentContext.startActivity(new Intent(currentContext, MainActivity.class));
    }

}
