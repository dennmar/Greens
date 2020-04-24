package com.example.dennis.greens;

import android.content.Intent;
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
                String usernameInput = usernameText.getText().toString();
                String passwordInput = passwordText.getText().toString();

                JsonObject postJson = new JsonObject();
                postJson.addProperty("username", usernameInput);
                postJson.addProperty("password", passwordInput);

                DatabaseService service = RestClient.getInstance(getContext())
                        .getDbService();
                Call<JsonObject> loginUser = service.login(postJson);
                loginUser.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call,
                            Response<JsonObject> response) {
                        if (response.code() == 200) {
                            JsonObject respBody = response.body();
                            LoginSession.getInstance(getContext()).setUserInfo(
                                    respBody.get("username").getAsString(),
                                    respBody.get("user_id").getAsInt(),
                                    respBody.get("refresh_token").getAsString(),
                                    respBody.get("access_token").getAsString()
                            );
                            enterGreens();
                        }
                        else {
                            String errMsg = "submit: Unexpected code " +
                                    response.code();
                            Log.d("LoginFragment", errMsg);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d("LoginFragment", "submit: " + t.toString());
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

    private void enterGreens() {
        Intent greensIntent = new Intent(getActivity(), GreensActivity.class);

        // prevent back button from going to login
        greensIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(greensIntent);
    }
}
