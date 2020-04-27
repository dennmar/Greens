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

/**
 * The fragment that displays the user's expenses.
 */
public class ExpenseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Instantiate the user interface view and set on-click listeners for buttons.
     *
     * @param inflater           the layout inflater to inflate views
     * @param container          the parent view to attach to
     * @param savedInstanceState the previously saved state of the fragment
     * @return the view for the user interface
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.expense_frag_view,
                container, false);

        setExpensesListener(rootView);
        setLogoutListener(rootView);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Set an on-click listener for the expenses button to display the user's
     * expenses.
     *
     * @param rootView the view for the user interface
     */
    private void setExpensesListener(View rootView) {
        Button expensesBtn = rootView.findViewById(R.id.expensesButton);
        expensesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpenses();
            }
        });
    }

    /**
     * Set an on-click listener for the logout button to logout the user.
     *
     * @param rootView the view for the user interface
     */
    private void setLogoutListener(View rootView) {
        Button logoutBtn = rootView.findViewById(R.id.expLogoutButton);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginSession.getInstance(getContext()).close();
                exitGreens();
            }
        });
    }

    /**
     * Redirect the user out of the main activity.
     */
    private void exitGreens() {
        Intent logoutIntent = new Intent(getActivity(), LoginActivity.class);

        // prevent back button from going to main activity
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(logoutIntent);
    }

    /**
     * Display the user's expenses.
     */
    private void showExpenses() {
        LoginSession sess = LoginSession.getInstance(getContext());
        RestClient restClient = RestClient.getInstance(getContext());
        int userId = sess.getUserId();

        Call<JsonObject> getExpenses = restClient.getAPIService().getExpenses(
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
                    Log.v("ExpenseFragment", "showExpenses: " + expensesStr.toString());
                }
                else {
                    Log.v("ExpenseFragment", "showExpenses: unexpected code "
                            + response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                StackTraceElement[] st = t.getStackTrace();
                Log.d("ExpenseFragment", "showExpenses: " + t.toString());
                for (int i = 0; i < st.length; i++) {
                    Log.d("ExpenseFragment", st[i].toString());
                }
            }
        });
    }
}

