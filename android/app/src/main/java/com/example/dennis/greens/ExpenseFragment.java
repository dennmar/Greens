package com.example.dennis.greens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.expense_frag_view,
                container, false);

        Button expensesBtn = rootView.findViewById(R.id.expensesButton);
        expensesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpenses();
            }
        });

        Button logoutBtn = rootView.findViewById(R.id.expLogoutButton);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginSession.getInstance(getContext()).close();
                exitGreens();
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void exitGreens() {
        Intent logoutIntent = new Intent(getActivity(), LoginActivity.class);

        // prevent back button from going to login
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(logoutIntent);
    }

    private void showExpenses() {
        LoginSession sess = LoginSession.getInstance(getContext());
        RestClient restClient = RestClient.getInstance(getContext());
        int userId = sess.getUserId();

        Call<JsonObject> getExpenses = restClient.getDbService().getExpenses(
                "Bearer " + sess.getAccessToken(),
                userId
        );
        getExpenses.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call,
                    Response<JsonObject> response) {
                if (response.code() == 200) {
                    String expensesStr = response.body().get("expenses")
                            .getAsJsonArray().toString();
                    Log.v("ExpenseFragment", "showExpenses: " + expensesStr);
                }
                else {
                    Log.v("ExpenseFragment", "showExpenses: unexpected code "
                            + response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("ExpenseFragment", "showExpenses: " + t.toString());
            }
        });
    }
}

