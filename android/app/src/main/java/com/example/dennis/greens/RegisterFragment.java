package com.example.dennis.greens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                String usernameInput = usernameText.getText().toString();
                String emailInput = emailText.getText().toString();
                String passwordInput = passwordText.getText().toString();
                String password2Input = password2Text.getText().toString();

                JsonObject postJson = new JsonObject();
                postJson.addProperty("username", usernameInput);
                postJson.addProperty("email", emailInput);
                postJson.addProperty("password", passwordInput);

                APIService service = RestClient.getInstance(getContext())
                        .getAPIService();
                Call<JsonObject> createUser = service.createUser(postJson);
                createUser.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call,
                            Response<JsonObject> response) {
                        if (response.code() == 200) {
                            FragmentDisplayActivity currActivity =
                                    (FragmentDisplayActivity) getActivity();
                            currActivity.replaceFragment(
                                    R.id.loginActFragContainer,
                                    new InitialFragment()
                            );
                        }
                        else {
                            String errMsg = "submit: Unexpected code " +
                                    response.code();
                            Log.d("RegisterFragment", errMsg);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call,
                            Throwable t) {
                        StackTraceElement[] st = t.getStackTrace();
                        Log.d("RegisterFragment", "submit: " + t.toString());
                        for (int i = 0; i < st.length; i++) {
                            Log.d("RegisterFragment", st[i].toString());
                        }
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
