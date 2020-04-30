package com.example.dennis.greens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * The first fragment displayed to the user upon starting the app.
 */
public class InitialFragment extends Fragment {
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
        final View rootView = inflater.inflate(R.layout.initial_frag_view,
                container, false);

        if (LoginSession.getInstance(getContext()).isLoggedIn()) {
            enterGreens();
        }
        else {
            setLoginListener(rootView);
            setRegisterListener(rootView);
        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Set an on-click listener on the login button to direct the user to the
     * login fragment.
     *
     * @param rootView the view for the user interface
     */
    private void setLoginListener(View rootView) {
        Button loginBtn = rootView.findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentDisplayActivity currActivity =
                        (FragmentDisplayActivity)getActivity();
                currActivity.replaceFragment(R.id.loginActFragContainer,
                        new LoginFragment());
            }
        });
    }

    /**
     * Set an on-click listener on the register button to direct the user to
     * the register fragment.
     *
     * @param rootView the view for the user interface
     */
    private void setRegisterListener(View rootView) {
        Button registerBtn = rootView.findViewById(R.id.registerButton);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentDisplayActivity currActivity =
                        (FragmentDisplayActivity)getActivity();
                currActivity.replaceFragment(R.id.loginActFragContainer,
                        new RegisterFragment());
            }
        });
    }

    /**
     * Redirect the user to the main activity.
     */
    private void enterGreens() {
        Intent greensIntent = new Intent(getActivity(), GreensActivity.class);

        // prevent back button from going to login
        greensIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(greensIntent);
    }
}
