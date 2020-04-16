package com.example.dennis.greens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEventSource;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.register_frag_view,
                container, false);

        final EditText usernameText = rootView.findViewById(R.id.registerUsernameText);
        final EditText emailText = rootView.findViewById(R.id.registerEmailText);
        final EditText passwordText = rootView.findViewById(R.id.registerPasswordText);
        final EditText password2Text = rootView.findViewById(R.id.registerPassword2Text);

        Button submitBtn = rootView.findViewById(R.id.registerSubmitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add validation
                Map<String, String> body = new HashMap();
                body.put("username", usernameText.getText().toString());
                body.put("email", emailText.getText().toString());
                body.put("password", passwordText.getText().toString());
                JSONObject postJson = new JSONObject(body);

                APIRequest req = new APIRequest(
                    getContext(),
                    Request.Method.POST,
                    getString(R.string.api_root_url) + "user/",
                    postJson,
                    true
                );

                req.send(new ResponseCallback() {
                    @Override
                    public void onResponse(JSONObject response) {
                        FragmentDisplayActivity currActivity =
                                (FragmentDisplayActivity)getActivity();
                        currActivity.replaceFragment(
                            R.id.loginActFragContainer,
                            new InitialFragment());
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errStr = "submitBtn: " + error.toString();
                        Log.d("RegisterFragment", errStr);
                    }
                });
           }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
