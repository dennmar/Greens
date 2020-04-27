package com.example.dennis.greens;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * The singleton session for the currently logged in user.
 */
public class LoginSession {
    private static LoginSession instance;
    private static Context context;
    private SharedPreferences sharedPrefs;

    /**
     * Find the shared preferences containing the user's login session info.
     *
     * @param ctx the context for the activity
     */
    private LoginSession(Context ctx) {
        context = ctx;
        sharedPrefs = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
    }

    /**
     * Get the single instance of the user's login session.
     *
     * @param ctx the context for the activity
     * @return the user's login session
     */
    public static synchronized LoginSession getInstance(Context ctx) {
        if (instance == null) {
            instance = new LoginSession(ctx);
        }

        return instance;
    }

    /**
     * Set the info for the user's login session.
     *
     * @param username     the username of the user
     * @param userId       the id of the user in the database
     * @param refreshToken the api refresh token for the user
     * @param accessToken  the api access token for the user
     */
    public void setUserInfo(String username, int userId, String refreshToken,
            String accessToken) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("username", username);
        editor.putInt("user_id", userId);
        editor.putString("refresh_token", refreshToken);
        editor.putString("access_token", accessToken);
        editor.apply();
    }

    /**
     * Get the username of the currently logged in user.
     *
     * @return the username
     */
    public String getUsername() {
        return sharedPrefs.getString("username", null);
    }

    /**
     * Get the user id of the currently logged in user.
     *
     * @return the user id
     */
    public int getUserId() {
        return sharedPrefs.getInt("user_id", -1);
    }

    /**
     * Get the refresh token for the currently logged in user.
     *
     * @return the refresh token
     */
    public String getRefreshToken() {
        return sharedPrefs.getString("refresh_token", null);
    }

    /**
     * Get the access token for the currently logged in user.
     *
     * @return the access token
     */
    public String getAccessToken() {
        return sharedPrefs.getString("access_token", null);
    }

    /**
     * Set the access token for the currently logged in user.
     *
     * @param accessToken the value for the access token of the user
     */
    public void setAccessToken(String accessToken) {
        sharedPrefs.edit().putString("access_token", accessToken);
    }

    /**
     * Close the login session.
     */
    public void close() {
        sharedPrefs.edit().clear().commit();
    }
}
