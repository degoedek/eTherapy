package com.colinelliott.newscratch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class ActivitySelectionFragment extends Fragment {
Button therapy, stretching;
    /*public ActivitySelectionFragment() {
        // Required empty public constructor
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_selection, container, false);
        therapy = view.findViewById(R.id.btn_therapy);
        stretching = view.findViewById(R.id.btn_stretching);

        //Therapy Button
        therapy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ConnectionActivity.class);
                startActivity(intent);
                //getActivity().finish();
            }
        });
        /*
        // Stretch Button
        stretching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(),StretchActivity.class);
                startActivity(intent);
            }
        });
        */

        return view;
    }
}