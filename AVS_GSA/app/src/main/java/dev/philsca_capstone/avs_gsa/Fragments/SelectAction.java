package dev.philsca_capstone.avs_gsa.Fragments;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dev.philsca_capstone.avs_gsa.Enum.Action;
import dev.philsca_capstone.avs_gsa.Models.Notif;
import dev.philsca_capstone.avs_gsa.R;
import dev.philsca_capstone.avs_gsa.SelectedInstances.UserSelectedAction;

public class SelectAction extends Fragment {

    public SelectAction() {
    }


    MenuItem bell;
    ValueEventListener mNotificationListener;
    DatabaseReference mNotificationRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_select_action, container, false);
        setHasOptionsMenu(true);
        Button btnReservation = v.findViewById(R.id.btnReservation);
        Button btnTracking = v.findViewById(R.id.btnTracking);

        btnReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UserSelectedAction(Action.RESERVATION);
                MyReservationFragment frag = new MyReservationFragment();
                getActivity().getFragmentManager().beginTransaction().replace(R.id.empty_container,frag).addToBackStack(null).commit();
            }
        });

        btnTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tracking tracking = new Tracking();
                getActivity().getFragmentManager().beginTransaction().replace(R.id.empty_container,tracking).addToBackStack(null).commit();
                /*new UserSelectedAction(Action.TRACKING);
                SelectAirline frag = new SelectAirline();
                getActivity().getFragmentManager().beginTransaction().replace(R.id.empty_container,frag).addToBackStack(null).commit();*/

            }
        });


        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Home");
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        bell = menu.findItem(R.id.action_bell);
        addNotificationListener();
    }


    private void addNotificationListener() {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String id = FirebaseAuth.getInstance().getUid();
        mNotificationRef = db.getReference("Users/" + id + "/Notification");
       mNotificationListener =  mNotificationRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean hasNew =false;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Notif notif = ds.getValue(Notif.class);
                    if(!notif.isOpened()){
                        hasNew = true;
                        break;
                    }
                }
                if(hasNew){bell.setIcon(R.drawable.ic_bell_red);}
                else{bell.setIcon(R.drawable.ic_bell);}

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(mNotificationListener != null) {mNotificationRef.removeEventListener(mNotificationListener);}
    }
}