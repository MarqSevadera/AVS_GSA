package dev.philsca_capstone.avs_gsa.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import dev.philsca_capstone.avs_gsa.Adapters.NotifAdapter;
import dev.philsca_capstone.avs_gsa.Models.Notif;
import dev.philsca_capstone.avs_gsa.Models.Reservation;
import dev.philsca_capstone.avs_gsa.R;
import dev.philsca_capstone.avs_gsa.SelectedInstances.UserSelectedNotif;

public class NotifBodyFragment extends Fragment {

    MenuItem actionBell;
    TextView tvId,tvStatus,tvMessage,tvFlight,tvAirline,tvDestination,tvFlighNo, tvAWB, tvDesc, tvDimension, tvWeight, tvPieces, tvConame, tvConAddress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_notif_body , container,false);
        setHasOptionsMenu(true);
        tvId = v.findViewById(R.id.tvId);
        tvStatus = v.findViewById(R.id.tvStatus);
        tvMessage = v.findViewById(R.id.tvMessage);
        tvFlight = v.findViewById(R.id.tvFlight);

        tvAWB = v.findViewById(R.id.tvAWB);
        tvAirline = v.findViewById(R.id.tvAirline);
        tvDestination = v.findViewById(R.id.tvDestination);
        tvFlighNo = v.findViewById(R.id.tvFlightNo);
        tvDesc = v.findViewById(R.id.tvDesc);
        tvDimension = v.findViewById(R.id.tvDimension);
        tvWeight = v.findViewById(R.id.tvWeight);
        tvPieces = v.findViewById(R.id.tvPieces);
        tvConame = v.findViewById(R.id.tvConame);
        tvConAddress = v.findViewById(R.id.tvConAddress);

        Notif notif = UserSelectedNotif.getSelectedNotif();
        tvId.setText("Reservation Id: " + notif.getReservationId());
        String heading = "Reservation " + notif.getStatus() + "!";
        if(TextUtils.equals(notif.getStatus(), "Shipped")){heading = "Your cargo was shipped!";}
        if(TextUtils.equals(notif.getStatus(),"Arrived")){heading = "Your cargo has arrived!"; }

        tvStatus.setText(heading);
        tvFlight.setText(notif.getFlight());
        tvAWB.setText("AWB: " + notif.getAWB());
        tvMessage.setText(notif.getMessage());

        retrieveData(notif.getReservationId());
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        actionBell = menu.findItem(R.id.action_bell);
        actionBell.setVisible(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Notification");


    }

    @Override
    public void onDetach() {
        super.onDetach();
        UserSelectedNotif.resetNotif();
        actionBell.setVisible(true);
    }



    private void retrieveData(final String uid){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Reservation/" + uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Reservation reservation =  dataSnapshot.getValue(Reservation.class);
                tvAirline.setText("Airline: " + reservation.getAirline());
                tvDestination.setText("Destination: " + reservation.getOriginName() + " - " + reservation.getDestinationName());
                tvFlighNo.setText("Flight Number: " + reservation.getFlightCode());
                tvDesc.setText("Cargo Description: " + reservation.getDesc());
                tvDimension.setText("Dimension: " + reservation.getLength() + " x " + reservation.getWidth() + " x " + reservation.getHeight());
                tvWeight.setText("Weight: " + reservation.getWeight() + " kg");
                tvPieces.setText("Pieces: " +reservation.getPieces());
                tvConame.setText("Consignee Name: " + reservation.getConsigneeName());
                tvConAddress.setText("Consignee Address: " + reservation.getConsigneeAddress());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
