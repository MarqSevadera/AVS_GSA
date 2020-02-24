package dev.philsca_capstone.avs_gsa.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import dev.philsca_capstone.avs_gsa.Fragments.Splash;
import dev.philsca_capstone.avs_gsa.R;

public class MainActivity extends AppCompatActivity {

    private static Activity firstInstance = null;
    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        firstInstance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);
        ImageView splash= findViewById(R.id.splash);
        Glide.with(this).asGif().load(R.drawable.moving_logo).into(splash);
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this , Home.class);
            startActivity(intent);
            finish();
            return;
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainFragment mainFragment = new MainFragment();
                    getFragmentManager().beginTransaction().replace(R.id.splash_container,mainFragment).commitAllowingStateLoss();
                }

            }, SPLASH_TIME_OUT);
        }



    }


    public static Activity getInstance(){
        return firstInstance;
    }


}
