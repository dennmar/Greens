package com.example.dennis.greens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_frag_view,
                container, false);

        final EditText usernameText = rootView.findViewById(R.id.loginUsernameText);
        final EditText passwordText = rootView.findViewById(R.id.loginPasswordText);

        Button submitBtn = rootView.findViewById(R.id.loginSubmitButton);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> body = new HashMap();
                body.put("username", usernameText.getText().toString());
                body.put("password", passwordText.getText().toString());

                JSONObject postJson = new JSONObject(body);
                JsonObjectRequest jsonObjRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    getString(R.string.api_root_url) + "auth/login",
                    postJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            enterGreens();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: handle error
                        }
                    });

                // NOTE: should set retry policy to prevent sending twice
                ReqQueue.getInstance(getContext()).add(jsonObjRequest);
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void enterGreens() {
        Intent greensIntent = new Intent(getActivity(), GreensActivity.class);

        // prevent back button from going to login
        greensIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(greensIntent);
    }
}
