package com.example.dennis.greens;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface ResponseCallback {
    void onResponse(JSONObject response);
    void onErrorResponse(VolleyError error);
}
