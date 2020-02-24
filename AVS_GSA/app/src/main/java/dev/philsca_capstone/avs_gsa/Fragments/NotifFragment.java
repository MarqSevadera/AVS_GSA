package dev.philsca_capstone.avs_gsa.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import dev.philsca_capstone.avs_gsa.Adapters.AirlineAdapter;
import dev.philsca_capstone.avs_gsa.Adapters.NotifAdapter;
import dev.philsca_capstone.avs_gsa.Models.Airline;
import dev.philsca_capstone.avs_gsa.Models.Notif;
import dev.philsca_capstone.avs_gsa.R;

public class NotifFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private NotifAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private List<Notif> mNotifLists;
    private ValueEventListener mListener;
    private TextView tvEmpty;


    MenuItem bell;
    ValueEventListener mNotificationListener;
    DatabaseReference mNotificationRef;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_notif , container,false);
        setHasOptionsMenu(true);
        tvEmpty = v.findViewById(R.id.tvEmpty);
        mRecyclerView =  v.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mNotifLists = new ArrayList<>();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users/"+uid+"/Notification");


        mListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mNotifLists.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Notif notif = snapshot.getValue(Notif.class);
                    mNotifLists.add(notif);
                }
                mAdapter = new NotifAdapter(getActivity(),mNotifLists);
                mRecyclerView.setAdapter(mAdapter);
                if(mNotifLists.isEmpty()){
                    tvEmpty.setVisibility(View.VISIBLE);
                }
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Notifacations");
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
        if(mListener != null) {mDatabaseRef.removeEventListener(mListener);}
        if(mNotificationListener != null){mNotificationRef.removeEventListener(mNotificationListener);}
    }
}
