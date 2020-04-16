package com.example.dennis.greens;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccessToken {
    private static AccessToken instance;
    private static Context context;
    private String token;

    private AccessToken(Context ctx) {
        context = ctx;
        token = null;
    }

    public static synchronized AccessToken getInstance(Context ctx) {
        if (instance == null) {
            instance = new AccessToken(ctx);
        }

        return instance;
    }

    private void updateToken(final ResponseCallback callback) {
        Log.v("AccessToken", "updateToken: Updating token");
        Map<String, String> body = new HashMap();
        body.put("username",
                context.getResources().getString(R.string.api_username));
        body.put("password",
                context.getResources().getString(R.string.api_password));
        JSONObject postJson = new JSONObject(body);

        APIRequest req = new APIRequest(
            context,
            Request.Method.POST,
            context.getResources().getString(R.string.api_root_url) +
                "auth/token",
            postJson,
            false
        );

        req.send(new ResponseCallback() {
            @Override
            public void onResponse(JSONObject response) {
                token = response.optString("token");
                callback.onResponse(response);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("AccessToken", "updateToken(): " + error.toString());
                callback.onErrorResponse(error);
            }
        });
    }

    public String refreshToken(Context ctx, ResponseCallback callback) {
        instance = new AccessToken(ctx);
        updateToken(callback);

        return token;
    }

    public void useToken(ResponseCallback callback) {
        if (token == null) {
            updateToken(callback);
        }
        else {
            // create a response using the token
            Map<String, String> fakeResp = new HashMap();
            fakeResp.put("token", token);
            JSONObject tokenJson = new JSONObject(fakeResp);
            callback.onResponse(tokenJson);
        }
    }

    public String getToken() {
        if (token == null) {
            updateToken(new ResponseCallback() {
                @Override
                public void onResponse(JSONObject response) {}

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("AccessToken", "getToken(): " + error.toString());
                }
            });
        }

        return token;
    }
}
