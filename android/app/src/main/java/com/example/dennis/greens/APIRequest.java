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
    private boolean passRefresh;

    public APIRequest(Context ctx, int m, String url, JSONObject body,
            boolean protectedRoute, boolean useRefreshToken) {
        context = ctx;
        method = m;
        apiUrl = url;
        reqBody = body;
        needToken = protectedRoute;
        passRefresh = useRefreshToken;
    }

    public void send(final ResponseCallback callback) {
        // NOTE: should set retry policy for volley to avoid sending twice
        boolean retryWithRefresh = needToken && !passRefresh;
        addRequest(callback, retryWithRefresh);
    }

    private void resend(final ResponseCallback callback) {
        AccessToken.getInstance(context).refreshAccess(new ResponseCallback() {
            @Override
            public void onResponse(JSONObject response) {
                addRequest(callback, false);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("APIRequest", "resend: " + error.toString());
            }
        });
    }

    private void addRequest(final ResponseCallback callback,
                            final boolean retryWithRefresh) {
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
                    // TODO: should only retry if error is for expired token
                    if (retryWithRefresh) {
                        Log.v("APIRequest", "addRequest: RESENDING");
                        resend(callback);
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    boolean isPostReq = method == Request.Method.POST;
                    boolean isPutReq = method == Request.Method.PUT;

                    if (needToken) {
                        LoginSession sess = LoginSession.getInstance(context);
                        String token = passRefresh ?
                                sess.getRefreshToken() :
                                AccessToken.getInstance(context).getToken();

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
