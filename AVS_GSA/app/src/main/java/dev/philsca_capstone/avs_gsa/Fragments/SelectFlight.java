package dev.philsca_capstone.avs_gsa.Fragments;

import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dev.philsca_capstone.avs_gsa.Adapters.FlightAdapter;

import dev.philsca_capstone.avs_gsa.Models.Airline;
import dev.philsca_capstone.avs_gsa.Models.Flight;
import dev.philsca_capstone.avs_gsa.R;
import dev.philsca_capstone.avs_gsa.SelectedInstances.UserSelectedAirline;

public class SelectFlight extends Fragment {

    private RecyclerView mRecyclerView;
    private FlightAdapter mAdapter;
    private DatabaseReference flightRef;
    private List<Flight> mFlightList;
    private ValueEventListener mListener;
    private Airline mSelectedAirline;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_select_flight, container, false);

        mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mSelectedAirline = UserSelectedAirline.getSelectedAirline();
        ImageView logo = v.findViewById(R.id.imageView);
        Glide.with(getActivity()).load(mSelectedAirline.getImage()).into(logo);

        listenToFlightTable();

        setHasOptionsMenu(true);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Select Flight");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        UserSelectedAirline.resetAirline();
        if(mListener != null) flightRef.removeEventListener(mListener);

    }


    public void listenToFlightTable(){
        mFlightList = new ArrayList<>();
        flightRef = FirebaseDatabase.getInstance().getReference("Airline").child(mSelectedAirline.getUid()).child("Flight");
        mListener = flightRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFlightList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Flight flight  = snapshot.getValue(Flight.class);
                    mFlightList.add(flight);
                }
                mAdapter = new FlightAdapter(getActivity() ,mFlightList);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
