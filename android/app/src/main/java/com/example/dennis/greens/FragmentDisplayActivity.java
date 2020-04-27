package com.example.dennis.greens;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * An activity that displays fragments.
 */
public class FragmentDisplayActivity extends AppCompatActivity {
    /**
     * Replace the fragment of a container with another fragment.
     *
     * @param fragContainerId the id of the container that will display the
     *                        fragment
     * @param nextFragment    the fragment to switch to
     */
    public void replaceFragment(int fragContainerId, Fragment nextFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragContainerId, nextFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Add a fragment to a container to be displayed.
     *
     * @param fragContainerId the id of the container that will display the
     *                        fragment
     * @param frag            the fragment to be added to the container
     */
    public void addFragment(int fragContainerId, Fragment frag) {
        getSupportFragmentManager().beginTransaction().add(fragContainerId,
                frag).commit();
    }
}
