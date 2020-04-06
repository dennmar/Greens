package com.example.dennis.greens;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ReqQueue {
    private static ReqQueue instance;
    private RequestQueue queue;
    private static Context context;

    private ReqQueue(Context ctx) {
        context = ctx;
        queue = getRequestQueue();
    }

    public static synchronized ReqQueue getInstance(Context ctx) {
        if (instance == null) {
            instance = new ReqQueue(ctx.getApplicationContext());
        }

        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(context.getApplicationContext());
        }

        return queue;
    }

    public <T> void add(Request<T> req) {
        getRequestQueue().add(req);
    }
}
