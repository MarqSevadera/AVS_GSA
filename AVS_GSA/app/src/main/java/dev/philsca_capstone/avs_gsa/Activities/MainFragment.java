package dev.philsca_capstone.avs_gsa.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import dev.philsca_capstone.avs_gsa.R;

public class MainFragment extends Fragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_main, container, false);

        Button btnSignup = v.findViewById(R.id.btnSignup);
        Button btnLogin = v.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
       return v;

    }

    @Override
    public void onClick(View view){
        int id = view.getId();

        switch(id){
            case R.id.btnLogin: {
                Intent intent = new Intent(getActivity() , Login.class);
                startActivity(intent);
            }
            break;
            case R.id.btnSignup: {
                Intent intent = new Intent(getActivity(), Signup.class);
                startActivity(intent);
            }
        }
    }
}

