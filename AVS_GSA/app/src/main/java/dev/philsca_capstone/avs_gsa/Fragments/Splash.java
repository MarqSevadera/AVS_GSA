package dev.philsca_capstone.avs_gsa.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import dev.philsca_capstone.avs_gsa.Activities.MainActivity;
import dev.philsca_capstone.avs_gsa.Activities.MainFragment;
import dev.philsca_capstone.avs_gsa.R;

public class Splash extends Fragment{

    private static int SPLASH_TIME_OUT = 3000;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_splash , container,false);
        ImageView splash= v.findViewById(R.id.splash);
        Glide.with((Context)getActivity()).asGif().load(R.drawable.moving_logo).into(splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainFragment mainFragment = new MainFragment();
                getFragmentManager().beginTransaction().replace(R.id.empty_container,mainFragment).commitAllowingStateLoss();
            }

        }, SPLASH_TIME_OUT);
        return v;
    }


}
