package dev.philsca_capstone.avs_gsa.Activities;

import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import dev.philsca_capstone.avs_gsa.AppUser;
import dev.philsca_capstone.avs_gsa.R;

public class Login extends AppCompatActivity {

    TextView tvSignup , tvForgot;
    Button btnLogin;
    MaterialEditText edtEmail,edtPassword;
    String email,password;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        tvSignup = findViewById(R.id.tvSignup);
        tvForgot = findViewById(R.id.tvForgot);
        tvSignup.setPaintFlags(tvSignup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvForgot.setPaintFlags(tvForgot.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();
    }


    public void onClick(View view){
        int id = view.getId();
        switch(id){
            case R.id.btnLogin: login();
            break;
            case R.id.tvSignup: {
                startActivity(new Intent(Login.this,Signup.class));
                finish();
            }
            break;
            case R.id.tvForgot: startActivity(new Intent(Login.this,ForgotPassword.class    ));
        }
    }

    public void login(){
        if(!isCredentialComplete()) return;

        final ProgressDialog mDialog = new ProgressDialog(Login.this);;
        mDialog.setMessage("Signing in..");
        mDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mDialog.dismiss();
                            if(mAuth.getCurrentUser().isEmailVerified()){
                                Intent intent = new Intent(Login.this,Home.class);
                                startActivity(intent);
                                AppUser.getInstance();
                                MainActivity.getInstance().finish();
                                finish();
                            }else{
                                mAuth.signOut();
                                AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
                                alertDialog.setTitle("Verify your account.");
                                alertDialog.setMessage("You need to verify your account before you can login.");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        } else {
                            mDialog.dismiss();
                            AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
                            alertDialog.setTitle("Invalid Credentials!");
                            alertDialog.setMessage("Invalid Email or Password.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();

                        }

                    }
                });
    }

    private boolean isCredentialComplete(){
        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();

        boolean result = true;
        if(TextUtils.isEmpty(email)){
            edtEmail.setError("Email is required!");
            result = false;
        }
        if(TextUtils.isEmpty(password)){
            edtPassword.setError("Password is required!");
            result = false;
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



}
