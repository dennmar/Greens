package com.example.dennis.greens;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginSession {
    private static LoginSession instance;
    private static Context context;
    private SharedPreferences sharedPrefs;

    private LoginSession(Context ctx) {
        context = ctx;
        sharedPrefs = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
    }

    public static synchronized LoginSession getInstance(Context ctx) {
        if (instance == null) {
            instance = new LoginSession(ctx);
        }

        return instance;
    }

    public void setUserInfo(String username, int userId, String refreshToken,
            String accessToken) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("username", username);
        editor.putInt("user_id", userId);
        editor.putString("refresh_token", refreshToken);
        editor.putString("access_token", accessToken);
        editor.apply();
    }

    public String getUsername() {
        return sharedPrefs.getString("username", null);
    }

    public int getUserId() {
        return sharedPrefs.getInt("user_id", -1);
    }

    public String getRefreshToken() {
        return sharedPrefs.getString("refresh_token", null);
    }

    public String getAccessToken() {
        return sharedPrefs.getString("access_token", null);
    }

    public void setAccessToken(String accessToken) {
        sharedPrefs.edit().putString("access_token", accessToken);
    }

    public void close() {
        sharedPrefs.edit().clear().commit();
    }
}
