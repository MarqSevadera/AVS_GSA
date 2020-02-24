package dev.philsca_capstone.avs_gsa.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import dev.philsca_capstone.avs_gsa.R;

public class ForgotPassword extends AppCompatActivity {

    Button button;
    MaterialEditText edtEmail;
    public ForgotPassword() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        button = findViewById(R.id.btnSubmit);
        edtEmail = findViewById(R.id.edtEmail);
    }


    public void onClick(View v){
        final ProgressDialog mDialog = new ProgressDialog(ForgotPassword.this);
        mDialog.setMessage("Please wait...");
        mDialog.show();
        final String email = edtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mDialog.dismiss();
            edtEmail.setError("Enter your email!");
        } else {
            mDialog.dismiss();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    if (task.isSuccessful()) {
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ForgotPassword.this);
                        mBuilder.setTitle("Reset password request sent!");
                        mBuilder.setMessage("We have sent an instruction to " + email + "to reset your password.");
                        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(ForgotPassword.this, Login.class));
                            }
                        });

                        AlertDialog alertDialog = mBuilder.create();
                        alertDialog.show();
                    }else{
                        edtEmail.setError("Invalid Email!");
                    }
                }

            });
        }
    }

}
