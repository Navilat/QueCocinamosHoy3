package com.example.avilaretamal.qucocinamoshoy;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

public class UserData {
    private static final String TOKEN = "token";
    private static final String USER_NAME = "user_name";
    private static final String USER_ID = "user_id";

    public static void RegisterToken(Context context, String response){
        String token = null;
        String user_id = null;
        String user_name = null;

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        JSONObject JsonResponse = null;
        try {
            JsonResponse = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            token = JsonResponse.getString(TOKEN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            user_name = JsonResponse.getString(USER_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            user_id = JsonResponse.getString(USER_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (token != null) {
            editor.putString(TOKEN, token);
            editor.putString(USER_NAME, user_name);
            editor.putString(USER_ID, user_id);
            editor.apply();
        }
    }

    public static String GetToken(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(TOKEN, null);
    }

    public static void RemoveToken(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.remove(TOKEN);
        editor.remove(USER_NAME);
        editor.remove(USER_ID);
        editor.apply();
    }
}
