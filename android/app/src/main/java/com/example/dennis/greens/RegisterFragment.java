package com.example.dennis.greens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

        Button submitBtn = rootView.findViewById(R.id.registerSubmitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent greensIntent = new Intent(getActivity(), GreensActivity.class);

               // prevent back button from going to login
               greensIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                       Intent.FLAG_ACTIVITY_CLEAR_TASK |
                       Intent.FLAG_ACTIVITY_NEW_TASK);

               startActivity(greensIntent);
           }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
