package dev.philsca_capstone.avs_gsa.Activities;

import android.app.AlertDialog;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dev.philsca_capstone.avs_gsa.AppUser;
import dev.philsca_capstone.avs_gsa.Fragments.NotifFragment;
import dev.philsca_capstone.avs_gsa.Fragments.SelectAction;
import dev.philsca_capstone.avs_gsa.Models.Notif;
import dev.philsca_capstone.avs_gsa.Models.User;
import dev.philsca_capstone.avs_gsa.R;

public class Home extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {



    private ProgressDialog mDialog;

    private boolean mOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUser.getInstance();
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Please wait..");
        setContentView(R.layout.layout_home);
        Toolbar myToolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getFragmentManager().addOnBackStackChangedListener(this);
        SelectAction selectActionFragment = new SelectAction();
        getFragmentManager().beginTransaction().replace(R.id.empty_container,selectActionFragment).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_logout){
            alertLogout();
            return true;
        }

        if(id == R.id.action_bell){
            if(mOpen){
                getFragmentManager().popBackStack();
                mOpen=false;
            }
            else {
                NotifFragment frag = new NotifFragment();
                getFragmentManager().beginTransaction().replace(R.id.empty_container, frag).addToBackStack(null).commit();
                mOpen=true;
            }
        }


        return super.onOptionsItemSelected(item);
    }

    private void alertLogout(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        mBuilder.setTitle("Logout");
        mBuilder.setMessage("Are you sure you want to Logout ?");
        mBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                FirebaseAuth.getInstance().signOut();
                AppUser.logoutUser();
                startActivity(new Intent(Home.this, Login.class));
                finish();
            }
        });
        mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();
    }


    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp(){
        boolean canGoBack = getFragmentManager().getBackStackEntryCount() > 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }




}
