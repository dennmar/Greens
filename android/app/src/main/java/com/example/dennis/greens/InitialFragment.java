package com.example.dennis.greens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.VolleyLog;

public class InitialFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.initial_frag_view,
                container, false);

        Button loginBtn = rootView.findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentDisplayActivity currActivity = (FragmentDisplayActivity)getActivity();
                currActivity.replaceFragment(R.id.loginActFragContainer, new LoginFragment());
            }
        });

        Button registerBtn = rootView.findViewById(R.id.registerButton);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentDisplayActivity currActivity = (FragmentDisplayActivity)getActivity();
                currActivity.replaceFragment(R.id.loginActFragContainer, new RegisterFragment());
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
