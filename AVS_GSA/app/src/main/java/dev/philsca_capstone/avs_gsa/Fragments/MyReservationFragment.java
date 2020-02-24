package dev.philsca_capstone.avs_gsa.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dev.philsca_capstone.avs_gsa.Adapters.AirlineAdapter;
import dev.philsca_capstone.avs_gsa.Adapters.ReservationAdapter;
import dev.philsca_capstone.avs_gsa.AppUser;
import dev.philsca_capstone.avs_gsa.Enum.Action;
import dev.philsca_capstone.avs_gsa.Models.Notif;
import dev.philsca_capstone.avs_gsa.Models.Reservation;
import dev.philsca_capstone.avs_gsa.R;
import dev.philsca_capstone.avs_gsa.SelectedInstances.UserSelectedAction;
import dev.philsca_capstone.avs_gsa.SelectedInstances.UserSelectedFlight;

public class MyReservationFragment extends Fragment {

    MenuItem actionNew;
    DatabaseReference mTblUserReservation;
    ValueEventListener mListener;
    private ArrayList<Reservation> reservationList;
    private ReservationAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView tvEmpty;


    MenuItem bell;
    ValueEventListener mNotificationListener;
    DatabaseReference mNotificationRef;

    public MyReservationFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_my_reservation, container, false);
        setHasOptionsMenu(true);

        mRecyclerView =  v.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        tvEmpty = v.findViewById(R.id.tvEmpty);


        listenToUserReservationTable();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
       actionNew =  menu.findItem(R.id.action_new);
       actionNew.setVisible(true);
       bell = menu.findItem(R.id.action_bell);
       addNotificationListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_new){
            SelectAirline frag = new SelectAirline();
            getFragmentManager().beginTransaction().replace(R.id.empty_container , frag).addToBackStack(null).commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        actionNew.setVisible(false);
        UserSelectedAction.resetSelectedAction();
        if(mListener != null){ mTblUserReservation.removeEventListener(mListener); }
        if(mNotificationListener != null){ mNotificationRef.removeEventListener(mNotificationListener); }


    }

    public void listenToUserReservationTable(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reservationList = new ArrayList<>();
        mTblUserReservation = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Reservation");
        mListener = mTblUserReservation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    reservationList.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Reservation r = snapshot.getValue(Reservation.class);
                        reservationList.add(r);
                    }
                mAdapter = new ReservationAdapter(getActivity(),reservationList);
                mRecyclerView.setAdapter(mAdapter);
                if(reservationList.isEmpty()){
                  tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My Reservations");
    }

    private void addNotificationListener() {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String id = FirebaseAuth.getInstance().getUid();
        mNotificationRef= db.getReference("Users/" + id + "/Notification");
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
