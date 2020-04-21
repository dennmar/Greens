package com.example.dennis.greens;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

    public void setUserInfo(String username, int userId, String refreshToken) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("username", username);
        editor.putInt("user_id", userId);
        editor.putString("refreshToken", refreshToken);
        editor.apply();
    }

    public String getUsername() {
        return sharedPrefs.getString("username", null);
    }

    public int getUserId() {
        return sharedPrefs.getInt("user_id", -1);
    }

    public String getRefreshToken() {
        return sharedPrefs.getString("refreshToken", null);
    }

    public void close() {
        sharedPrefs.edit().clear().commit();
    }
}
