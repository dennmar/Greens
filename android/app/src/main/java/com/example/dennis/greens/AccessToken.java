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
        token = LoginSession.getInstance(context).getAccessToken();
    }

    public static synchronized AccessToken getInstance(Context ctx) {
        if (instance == null) {
            instance = new AccessToken(ctx);
        }

        return instance;
    }

    public void refreshAccess(final ResponseCallback callback) {
        Log.v("AccessToken", "updateToken: Updating token");

        APIRequest req = new APIRequest(
                context,
                Request.Method.POST,
                context.getResources().getString(R.string.api_root_url) +
                        "auth/refresh/",
                null,
                true,
                true
        );

        req.send(new ResponseCallback() {
            @Override
            public void onResponse(JSONObject response) {
                token = response.optString("access_token");
                LoginSession.getInstance(context).setAccessToken(token);
                callback.onResponse(response);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMsg = "refreshAccessToken: " + error.toString();
                Log.d("AccessToken", errorMsg);
                callback.onErrorResponse(error);
            }
        });
    }

    public String getToken() {
        return token;
    }
}
