package com.example.dennis.greens;

import android.os.Bundle;

public class LoginActivity extends FragmentDisplayActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_view);

        /**
        InitialFragment initFrag = new InitialFragment();
        getSupportFragmentManager().beginTransaction().
                add(R.id.loginActFragContainer, initFrag).commit();
         */

        addFragment(R.id.loginActFragContainer, new InitialFragment());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
