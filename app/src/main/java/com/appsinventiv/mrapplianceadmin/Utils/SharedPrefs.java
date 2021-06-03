package com.appsinventiv.mrapplianceadmin.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.appsinventiv.mrapplianceadmin.ApplicationClass;
import com.appsinventiv.mrapplianceadmin.Models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by AliAh on 20/02/2018.
 */

public class SharedPrefs {


    private SharedPrefs() {

    }


    public static void setServicesList(List<String> itemList) {

        Gson gson = new Gson();
        String json = gson.toJson(itemList);
        preferenceSetter("services", json);
    }

    public static ArrayList getServicesList() {
        Gson gson = new Gson();
        ArrayList<String> playersList = (ArrayList<String>) gson.fromJson(preferenceGetter("services"),
                new TypeToken<ArrayList<String>>() {
                }.getType());
        return playersList;
    }


    public static void setPhone(String value) {

        preferenceSetter("phone", value);
    }

    public static String getPhone() {
        return preferenceGetter("phone");
    }

    public static void setName(String value) {

        preferenceSetter("name", value);
    }

    public static String getName() {
        return preferenceGetter("name");
    }

    public static void setUsername(String value) {

        preferenceSetter("username", value);
    }

    public static String getUsername() {
        return preferenceGetter("username");
    }

    public static void setIsLoggedIn(String value) {

        preferenceSetter("isLoggedIn", value);
    }

    public static String getIsLoggedIn() {
        return preferenceGetter("isLoggedIn");
    }


    public static void setFcmKey(String fcmKey) {
        preferenceSetter("fcmKey", fcmKey);
    }

    public static String getFcmKey() {
        return preferenceGetter("fcmKey");
    }

    public static void setPicUrl(String fcmKey) {
        preferenceSetter("picUrl", fcmKey);
    }

    public static String getPicUrl() {
        return preferenceGetter("picUrl");
    }


    public static void setUser(User model) {

        Gson gson = new Gson();
        String json = gson.toJson(model);
        preferenceSetter("customerModel", json);
    }

    public static User getUser() {
        Gson gson = new Gson();
        User customer = gson.fromJson(preferenceGetter("customerModel"), User.class);
        return customer;
    }


    public static void preferenceSetter(String key, String value) {
        SharedPreferences pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String preferenceGetter(String key) {
        SharedPreferences pref;
        String value = "";
        pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        value = pref.getString(key, "");
        return value;
    }

    public static void logout() {
        SharedPreferences pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
