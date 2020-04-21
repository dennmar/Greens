package com.example.dennis.greens;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class APIRequest {
    private Context context;
    private int method;
    private String apiUrl;
    private JSONObject reqBody;
    private boolean needToken;

    public APIRequest(Context ctx, int m, String url, JSONObject body,
            boolean protectedRoute) {
        context = ctx;
        method = m;
        apiUrl = url;
        reqBody = body;
        needToken = protectedRoute;
    }

    public void send(final ResponseCallback callback) {
        // NOTE: should set retry policy to avoid sending twice
        if (needToken) {
            AccessToken.getInstance(context).useToken(new ResponseCallback() {
                @Override
                public void onResponse(JSONObject response) {
                    addRequest(callback);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("APIRequest", error.toString());
                }
            });
        }
        else {
            addRequest(callback);
        }
    }

    private void addRequest(final ResponseCallback callback) {
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(
            method,
            apiUrl,
            reqBody,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    callback.onResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onErrorResponse(error);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    boolean isPostReq = method == Request.Method.POST;
                    boolean isPutReq = method == Request.Method.PUT;

                    if (needToken) {
                        String token = AccessToken.getInstance(context).getToken();
                        headers.put("Authorization", "Bearer " + token);
                    }
                    if (isPostReq || isPutReq) {
                        headers.put("Content-Type", "application/json");
                    }
                    return headers;
                }
            };

        ReqQueue.getInstance(context).add(jsonObjRequest);
    }
}
