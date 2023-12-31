package com.colinelliott.newscratch;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.colinelliott.newscratch.ui.login.LogInHubActivity;
import com.colinelliott.newscratch.ui.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeScreen extends Fragment {
Button welcomeButton;
ObjectAnimator notchMove, notchRotate;
AnimatorSet animatorSet;
ImageView surfaceNotch;
boolean showWelcome;
private int animationDuration = 2000;
    /*public WelcomeScreen() {
        // Required empty public constructor
    }
*/

/*    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_welcome_screen, container, false);

        //View Object Instantiations
        surfaceNotch = v.findViewById(R.id.notch_surface);
        welcomeButton=v.findViewById(R.id.btn_welcome);

        //Notch Animation
        notchMove = ObjectAnimator.ofFloat(surfaceNotch,"x",650);
        notchMove.setDuration(animationDuration);
        notchMove.setRepeatCount(ValueAnimator.INFINITE);
        notchMove.setRepeatMode(ValueAnimator.REVERSE);
        notchRotate = ObjectAnimator.ofFloat(surfaceNotch,"rotation",480);
        notchRotate.setDuration(animationDuration);
        notchRotate.setRepeatCount(ValueAnimator.INFINITE);
        notchRotate.setRepeatMode(ValueAnimator.REVERSE);
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(notchMove,notchRotate);
        animatorSet.start();

        //Welcome Button
        welcomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //startActivity(new Intent(getActivity(), LoginActivity.class));
                    startActivity(new Intent(getActivity(), LogInHubActivity.class));
            }
        });

        return v;
    }
}