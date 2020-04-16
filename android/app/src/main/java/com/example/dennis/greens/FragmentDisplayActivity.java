package com.example.dennis.greens;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class FragmentDisplayActivity extends AppCompatActivity {
    public void replaceFragment(int fragContainerId, Fragment nextFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragContainerId, nextFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addFragment(int fragContainerId, Fragment frag) {
        getSupportFragmentManager().beginTransaction().add(fragContainerId,
                frag).commit();
    }
}
