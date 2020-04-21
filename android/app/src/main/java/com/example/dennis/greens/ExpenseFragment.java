package com.example.dennis.greens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
                displayExpenses();
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

    private void displayExpenses() {
        Map<String, String> body = new HashMap();
        int userId = LoginSession.getInstance(getContext()).getUserId();

        APIRequest req = new APIRequest(
                getContext(),
                Request.Method.GET,
                getString(R.string.api_root_url) + "user/" + userId +
                    "/expense",
                null,
                true,
                false
        );

        req.send(new ResponseCallback() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("ExpenseFragment",
                        response.optJSONArray("expenses").toString());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ExpenseFragment", "displayExpenses: " +
                        error.toString());
            }
        });
    }
}

