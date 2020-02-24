package dev.philsca_capstone.avs_gsa.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;

import dev.philsca_capstone.avs_gsa.AppUser;
import dev.philsca_capstone.avs_gsa.Models.Notif;
import dev.philsca_capstone.avs_gsa.Models.Reservation;
import dev.philsca_capstone.avs_gsa.R;
import dev.philsca_capstone.avs_gsa.SelectedInstances.UserSelectedReservation;

public class DetailsFragment extends Fragment {

    ImageView logo;
    TextView tvStatus, tvFlightNo;
    MaterialEditText edtShipperName, edtShipperAddress , edtConsigneeName, edtConsigneeAddress,edtDesc;
    MaterialEditText edtLength , edtWidth , edtHeight;
    MaterialEditText edtPieces , edtWeight;
    MaterialAutoCompleteTextView edtOrigin , edtDestination;
    MaterialEditText edtShippingDate;
    ImageView imgCalendar;

    Button btnCancel;
    Reservation reservation;

    MenuItem bell;
    ValueEventListener mNotificationListener;
    DatabaseReference mNotificationRef;



    public DetailsFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_reservation_details, container, false);
        setHasOptionsMenu(true);
        logo = v.findViewById(R.id.imageView);
        imgCalendar = v.findViewById(R.id.calendar);
        tvStatus = v.findViewById(R.id.tvStatus);
        tvFlightNo = v.findViewById(R.id.tvFlightNo);
        edtDesc = v.findViewById(R.id.edtDesc);
        edtShipperName = v.findViewById(R.id.edtName);
        edtShipperAddress = v.findViewById(R.id.edtAddress);
        edtConsigneeName = v.findViewById(R.id.edtConName);
        edtConsigneeAddress = v.findViewById(R.id.edtConAddress);
        edtLength = v.findViewById(R.id.edtLength);
        edtWidth = v.findViewById(R.id.edtWidth);
        edtHeight = v.findViewById(R.id.edtHeight);
        edtPieces = v.findViewById(R.id.edtPieces);
        edtWeight = v.findViewById(R.id.edtWeight);
        edtOrigin = v.findViewById(R.id.edtOrigin);
        edtDestination = v.findViewById(R.id.edtDestination);
        edtShippingDate = v.findViewById(R.id.edtDate);
        btnCancel = v.findViewById(R.id.btnCancel);

        reservation = UserSelectedReservation.getSelectedReservation();
        setValues();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelReservation();
            }
        });


        return v;
    }


    @Override
    public void onDetach() {
        UserSelectedReservation.resetReservation();
        super.onDetach();
        if(mNotificationListener != null) {mNotificationRef.removeEventListener(mNotificationListener);}
    }


    private void setValues(){
        Glide.with(getActivity()).load(reservation.getImgUrl()).into(logo);
        edtDesc.setText(reservation.getDesc());
        edtShipperName.setText(reservation.getShipperName());
        edtShipperAddress.setText(reservation.getShipperAddress());
        edtConsigneeName.setText(reservation.getConsigneeName());
        edtConsigneeAddress.setText(reservation.getConsigneeAddress());
        edtLength.setText(String.valueOf(reservation.getLength()));
        edtWidth.setText(String.valueOf(reservation.getWidth()));
        edtHeight.setText(String.valueOf(reservation.getHeight()));
        edtPieces.setText(String.valueOf(reservation.getPieces()));
        edtWeight.setText(String.valueOf(reservation.getWeight()));
        edtOrigin.setText(reservation.getOriginCode());
        edtDestination.setText(reservation.getDestinationCode());
        edtShippingDate.setText(reservation.getShippingDate());
        tvStatus.setText(reservation.getStatus());
        tvFlightNo.setText(reservation.getFlightCode());
        if(TextUtils.equals(reservation.getStatus(),"Pending"))
            btnCancel.setEnabled(true);
    }

    private void cancelReservation(){
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Cancel Reservation?");
        alertDialog.setMessage("Are you sure you want to cancel your reservation?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                                .child(AppUser.getInstance().getUid()).child("Reservation").child(reservation.getUid());
                        ref.removeValue();
                        Toast.makeText(getActivity(),"Reservation was successfully cancelled!",Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {

                     }
        });
        alertDialog.show();
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
