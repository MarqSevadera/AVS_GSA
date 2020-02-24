package dev.philsca_capstone.avs_gsa.Fragments;


import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import dev.philsca_capstone.avs_gsa.AppUser;
import dev.philsca_capstone.avs_gsa.Models.Airline;
import dev.philsca_capstone.avs_gsa.Models.Location;
import dev.philsca_capstone.avs_gsa.Models.Notif;
import dev.philsca_capstone.avs_gsa.Models.Reservation;
import dev.philsca_capstone.avs_gsa.R;
import dev.philsca_capstone.avs_gsa.SelectedInstances.UserSelectedAirline;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewReservationFrament extends Fragment implements View.OnClickListener{

    ImageView logo;
    MaterialEditText edtShipperName, edtShipperAddress , edtConsigneeName, edtConsigneeAddress,edtDesc;
    MaterialEditText edtLength , edtWidth , edtHeight;
    MaterialEditText edtPieces , edtWeight;
    MaterialAutoCompleteTextView edtOrigin , edtDestination;
    MaterialEditText edtShippingDate;
    ImageView imgCalendar;

    Button btnSubmit;
    String strOrigin[]; //stores the code and name of the Location Object(ex. {Manila , MNL})
    String strDestination[];

    DatabaseReference dbRef;
    ValueEventListener mListener;

    MenuItem bell;
    ValueEventListener mNotificationListener;
    DatabaseReference mNotificationRef;

    private List<String> locationList;

    public NewReservationFrament() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.layout_reservation, container, false);
        logo = v.findViewById(R.id.imageView);
        Glide.with(getActivity()).load(UserSelectedAirline.getSelectedAirline().getImage()).into(logo);

        edtOrigin = v.findViewById(R.id.edtOrigin);
        edtDestination = v.findViewById(R.id.edtDestination);
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
        edtShippingDate = v.findViewById(R.id.edtDate);
        imgCalendar = v.findViewById(R.id.calendar);
        btnSubmit = v.findViewById(R.id.btnSubmit);

        //change filters of AutoCompleteTextView
        edtOrigin.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtDestination.setFilters(new InputFilter[]{new InputFilter.AllCaps()});



        //set pre values
        edtShipperName.setText(AppUser.getInstance().getUsername());
        edtShipperAddress.setText(AppUser.getInstance().getAddress());
        edtShippingDate.setText(getDateToday());

        btnSubmit.setOnClickListener(this);
        imgCalendar.setOnClickListener(this);

        retrieveDataFromDB();


        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Reservation");

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id){
            case R.id.btnSubmit:
                break;
            case R.id.calendar:{
                DialogFragment frag = new DatePickerFragment();
                frag.show(getFragmentManager(),"datePicker");
            }
        }
    }

    public void retrieveDataFromDB(){
        locationList = new ArrayList<>();
        dbRef = FirebaseDatabase.getInstance().getReference("Locations");
        mListener = dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                locationList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Location location = snapshot.getValue(Location.class);
                    String string = location.getCode()+ "-" + location.getName();
                    locationList.add(string);

                }

                ArrayAdapter<String> adapter  = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,locationList);
                edtDestination.setAdapter(adapter);
                edtOrigin.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        edtOrigin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strOrigin = parent.getItemAtPosition(position).toString().split("-");
                Toast.makeText(getActivity(),"PICKED: " + strOrigin[1] , Toast.LENGTH_SHORT).show();
                if(TextUtils.equals(strOrigin[0] , edtDestination.getText().toString())){
                    edtOrigin.setText("");
                    edtOrigin.setError("Invalid!");

                }else{ edtOrigin.setText(strOrigin[0]); } //put only the location code

            }
        });

        edtDestination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strDestination= parent.getItemAtPosition(position).toString().split("-");
                if(TextUtils.equals(strDestination[0] , edtOrigin.getText().toString())){
                    edtDestination.setText("");
                    edtDestination.setError("Invalid!");

                }else{ edtDestination.setText(strDestination[0]); } //put only the location code
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFormComplete()) return;
                if(!isInputValid()) return;

                addToDatabase();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(mListener != null){
            dbRef.removeEventListener(mListener);
        }
        if(mNotificationListener != null) mNotificationRef.removeEventListener(mNotificationListener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        bell = menu.findItem(R.id.action_bell);
        addNotificationListener();
    }

    private boolean isFormComplete(){

        String error = "This field is required!";
        boolean result = true;

        if(TextUtils.isEmpty(edtOrigin.getText().toString())){
            result = false;
            edtOrigin.setError(error);
        }
        if(TextUtils.isEmpty((edtDestination.getText().toString()))){
            result = false;
            edtDestination.setError(error);
        }

         MaterialEditText edtArr[] = {edtDesc , edtShipperName,edtShipperAddress, edtConsigneeName , edtConsigneeAddress ,
                 edtLength , edtHeight , edtWeight , edtWidth , edtPieces , edtShippingDate};

        for(int i = 0 ; i < edtArr.length ; i++){
            if(TextUtils.isEmpty(edtArr[i].getText().toString())){
                edtArr[i].setError(error);
                result = false;
            }
        }

        return result;

    }

    private boolean isInputValid(){
        boolean result = true;

        try {

            //Check Weight first (must be greater that 0) because weight can be a double
            double weight = Double.parseDouble(edtWeight.getText().toString());
            if(weight < 1 ){
                edtWeight.setError("Invalid Value!");
                result = false;
            }

            //check other edt text that accepts an integer value (must be greater than 0)
            MaterialEditText edtArr[] = {edtLength,edtWidth,edtHeight,edtPieces};
            for(int i = 0 ; i < edtArr.length ; i++){
                if(Integer.parseInt(edtArr[0].getText().toString()) < 1){
                    edtArr[0].setError("Invalid Value!");
                    result = false;
                }
            }


        }catch (NumberFormatException ex){
            Toast.makeText(getActivity() , "Invalid Input!" , Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

    private void addToDatabase(){

        //adding to Reservation Table
        String currentUser = AppUser.getInstance().getUid();
        Airline selectedAirline = UserSelectedAirline.getSelectedAirline();
        dbRef = FirebaseDatabase.getInstance().getReference("Reservation");
        Reservation reservation = new Reservation();
        reservation.setOriginCode(edtOrigin.getText().toString());
        reservation.setOriginName(strOrigin[1]);
        reservation.setDestinationCode(edtDestination.getText().toString());
        reservation.setDestinationName(strDestination[1]);
        reservation.setDesc(edtDesc.getText().toString());
        reservation.setShipperName(edtShipperName.getText().toString());
        reservation.setShipperAddress(edtShipperAddress.getText().toString());
        reservation.setConsigneeName(edtConsigneeName.getText().toString());
        reservation.setConsigneeAddress(edtConsigneeAddress.getText().toString());
        reservation.setLength(Integer.valueOf(edtLength.getText().toString()));
        reservation.setWidth(Integer.valueOf(edtWidth.getText().toString()));
        reservation.setHeight(Integer.valueOf(edtHeight.getText().toString()));
        reservation.setPieces(Integer.valueOf(edtPieces.getText().toString()));
        reservation.setWeight(Double.valueOf(edtWeight.getText().toString()));
        reservation.setShippingDate(edtShippingDate.getText().toString());
        reservation.setAirline(selectedAirline.getName());
        reservation.setImgUrl(selectedAirline.getImage());
        reservation.setUserId(currentUser);
        String uid = dbRef.push().getKey();
        reservation.setUid(uid);

        dbRef.child(uid).setValue(reservation);

        successDialog();

        //adding the Reservation to the user record
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(AppUser.getInstance().getUid()).child("Reservation").child(uid);
        dbRef.setValue(reservation);
    }

    private void successDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Reservation Submitted!");
        alertDialog.setMessage("Thank You! Your reservation was successfully submitted." +
                "               We will send you a confirmation once we evaluated your reservation.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //go back to my reservation fragment
                        getFragmentManager().popBackStack();
                        getFragmentManager().popBackStack();
                    }
                });
        alertDialog.show();
    }




    private String getDateToday(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return day + " / " + (month+1) + " / " + year;

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
