package dev.philsca_capstone.avs_gsa.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.security.MessageDigest;

import dev.philsca_capstone.avs_gsa.Models.User;
import dev.philsca_capstone.avs_gsa.R;

public class Signup extends AppCompatActivity {

    MaterialEditText edtUsername, edtCompany, edtEmail, edtPassword, edtConfirmPass, edtAddress, edtPhone;
    String username, company, email, password, confirm, address , phone;
    Button btnSignup;
    ProgressDialog mDialog;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        edtUsername = findViewById(R.id.edtUsername);
        edtCompany = findViewById(R.id.edtCompany);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPass = findViewById(R.id.edtConfirmPass);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhone = findViewById(R.id.edtPhone);
        btnSignup = findViewById(R.id.btnSignup);

        mDialog = new ProgressDialog(Signup.this);

        //initialize database
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean isCredentialsComplete(){
        username = edtUsername.getText().toString();
        company = edtCompany.getText().toString();
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();
        confirm = edtConfirmPass.getText().toString();
        address = edtAddress.getText().toString();
        phone = edtPhone.getText().toString();

        boolean result = true;
        if(TextUtils.isEmpty(username)){
            result = false;
            edtUsername.setError("Username is required!");
        }
        if(TextUtils.isEmpty(company)){
            result = false;
            edtCompany.setError("Company is required!");
        }
        if(TextUtils.isEmpty(email)){
            result = false;
            edtEmail.setError("Email is required!");
        }
        if(TextUtils.isEmpty(password)){
            result = false;
            edtPassword.setError("Password is required!");
        }
        if(TextUtils.isEmpty(confirm)){
            result = false;
            edtConfirmPass.setError("You need to confirm password!");
        }
        if(TextUtils.isEmpty(address)){
            result = false;
            edtAddress.setError("Address is required!");
        }
        if(TextUtils.isEmpty(phone)){
            result = false;
            edtPhone.setError("Phone number is required!");
        }

        return result;
    }

    public boolean passwordMatched(){
        if(TextUtils.equals(password,confirm))
            return true;
        else {
            edtConfirmPass.setError("Password does not match!");
            return false;
        }
    }

    public void onClick(View view){
        signup();

    }

    public void signup(){
        if(!isCredentialsComplete()) return;
        if(!passwordMatched()) return;

        mDialog.setMessage("Please wait..");
        mDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mDialog.dismiss();
                    firebaseUser = mAuth.getCurrentUser();
                    addToDatabase();
                    firebaseUser.sendEmailVerification().addOnCompleteListener(Signup.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showAlertDialog();
                            } else {
                                Toast.makeText(Signup.this,
                                        "Failed to send verification email.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    mDialog.dismiss();
                    edtEmail.setError("The email might be registered already or invalid!");
                }
            }
        });


    }

    public void showAlertDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(Signup.this).create();
        alertDialog.setTitle("Verify Account");
        alertDialog.setMessage("A verification email was successfully sent to your email adress.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mAuth.signOut();
                        startActivity(new Intent(Signup.this,Login.class));
                        finish();
                    }
                });
        alertDialog.show();

    }
    private void addToDatabase(){

        //adding to Users Table
        String currentUser = firebaseUser.getUid();
        dbRef = database.getReference("Users");
        User user = new User();
        user.setUsername(username);
        user.setAddress(address);
        user.setCompany(company);
        user.setEmail(email);
        user.setUid(currentUser);
        user.setPassword_hashed(encryptData(password));
        user.setPhoneNumber(phone);
        dbRef.child(currentUser).setValue(user);
    }

    private String encryptData(String data){
        String encryptedString;
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(data.getBytes());
            encryptedString = new String(messageDigest.digest());
        }catch (Exception e){
            encryptedString = data;
            Log.i("ERROR HASHING" , "Something went wrong..");
        }
        return encryptedString;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
