package dev.philsca_capstone.avs_gsa.Fragments;

import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import dev.philsca_capstone.avs_gsa.Models.Notif;
import dev.philsca_capstone.avs_gsa.UserAction;
import dev.philsca_capstone.avs_gsa.Adapters.AirlineAdapter;
import dev.philsca_capstone.avs_gsa.Models.Airline;
import dev.philsca_capstone.avs_gsa.R;

public class SelectAirline extends Fragment {

    private RecyclerView mRecyclerView;
    private AirlineAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private List<Airline> mAirlineLists;
    private ValueEventListener mListener;


    MenuItem bell;
    ValueEventListener mNotificationListener;
    DatabaseReference mNotificationRef;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Select Airline");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.layout_select_airline, container, false);
        setHasOptionsMenu(true);

        mRecyclerView =  v.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mAirlineLists = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Airline");
        mListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mAirlineLists.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Airline airline = snapshot.getValue(Airline.class);
                    mAirlineLists.add(airline);
                }
                mAdapter = new AirlineAdapter(getActivity(),mAirlineLists);
                mRecyclerView.setAdapter(mAdapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        bell = menu.findItem(R.id.action_bell);
        addNotificationListener();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        UserAction.resetAction();
        if(mListener != null) mDatabaseRef.removeEventListener(mListener);
        if(mNotificationListener != null) mNotificationRef.removeEventListener(mNotificationListener);
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
}
