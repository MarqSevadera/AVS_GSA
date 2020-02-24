package dev.philsca_capstone.avs_gsa.Fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.security.Key;

import dev.philsca_capstone.avs_gsa.Activities.Home;
import dev.philsca_capstone.avs_gsa.Models.Airline;
import dev.philsca_capstone.avs_gsa.Models.Flight;
import dev.philsca_capstone.avs_gsa.Models.Notif;
import dev.philsca_capstone.avs_gsa.R;
import dev.philsca_capstone.avs_gsa.SelectedInstances.UserSelectedAirline;
import dev.philsca_capstone.avs_gsa.SelectedInstances.UserSelectedFlight;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tracking extends Fragment {

    WebView webView;
    MaterialEditText edtSearch;
    Button btnTrack;


    MenuItem bell;
    ValueEventListener mNotificationListener;
    DatabaseReference mNotificationRef;

    public Tracking() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.layout_tracking, container, false);
        edtSearch = v.findViewById(R.id.edtSearch);
        btnTrack = v.findViewById(R.id.buttonTrack);
        webView = v.findViewById(R.id.webView);
        setHasOptionsMenu(true);
        if(UserSelectedFlight.getSelectedFlight() != null){
            String flightCode = UserSelectedFlight.getSelectedFlight().getCode();
            String splitted[] = flightCode.split(" ");
            trackCode(splitted);
        }




        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = edtSearch.getText().toString();
                String[] splitted = code.split(" ");

                if(splitted.length != 2){edtSearch.setError("Invalid Flight Code!"); return;}
                trackCode(splitted);
            }
        });

        addNotificationListener();

        return v;
    }



    private void trackCode(String[] splitted){

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching Data..");
        progressDialog.show();

        webView.requestFocus();
        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view , String url){
              /*  webView.loadUrl("javascript:(function() { " +
                        "var head = document.getElementsByClassName('layout-row__RowInner-s1uoco8s-1 ceJxq')[0].style.display='none'; " +
                        "var head = document.getElementsByClassName('layout-row__RowInner-s1uoco8s-1 ceJxq')[1].style.display='none'; " +
                        "var head = document.getElementsByClassName('layout-row__RowInner-s1uoco8s-1 ceJxq')[2].style.display='none'; " +
                        "var head = document.getElementsByClassName('layout-row__RowInner-s1uoco8s-1 ceJxq')[3].style.display='none'; " +
                        "var head = document.getElementsByClassName('compact-header__HeaderWrapper-s3e4xtb-0 cADmTZ')[0].style.display='none'; " +
                        "var head = document.getElementsByClassName('layout-footer__LayoutFooterWrapper-s1i4j3cg-0 SOAmc')[0].style.display='none'; " +
                        "var head = document.getElementsByClassName('mobile-apps-ad__RightColumn-u4xl7b-4 RqKHL')[0].style.display='none'; " +
                        "})()"
                );*/
                progressDialog.dismiss();
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.flightstats.com/v2/flight-tracker/"+splitted[0]+"/"+splitted[1]);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        UserSelectedFlight.resetFlight();
        if(mNotificationListener != null) {mNotificationRef.removeEventListener(mNotificationListener);}
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getView() == null){
            return;
        }

        webView.requestFocus();
        getView().setFocusableInTouchMode(true);
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK || event.getAction() == KeyEvent.KEYCODE_BACK && keyCode == KeyEvent.KEYCODE_BACK){

                    if(webView.canGoBack()) webView.goBack();
                    return true;
                }
                return false;
            }

        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Tracking");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        bell = menu.findItem(R.id.action_bell);
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
