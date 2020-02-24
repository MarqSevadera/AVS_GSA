package dev.philsca_capstone.avs_gsa.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import dev.philsca_capstone.avs_gsa.Fragments.NotifBodyFragment;
import dev.philsca_capstone.avs_gsa.Models.Notif;
import dev.philsca_capstone.avs_gsa.R;
import dev.philsca_capstone.avs_gsa.SelectedInstances.UserSelectedNotif;


public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.NotifViewHolder> {

    private Context mContext;
    private List<Notif> notifList;

    public NotifAdapter(Context mContext, List<Notif> notifList) {
        this.mContext = mContext;
        this.notifList = notifList;
    }


    @NonNull
    @Override
    public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_notif,null);

        return new NotifAdapter.NotifViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifViewHolder holder, int position) {
        final Notif notif = notifList.get(position);
        String status = notif.getStatus();
        String m = "";
        if(TextUtils.equals(status,"Arrived")){
            m = "Cargo has Arrived!";
        }else if(TextUtils.equals(status,"Shipped")){
            m = "Cargo has been Shipped!";
        }else{
            m = "Reservation was " + notif.getStatus() + "!";
        }
        holder.tvStatus.setText(m);

        holder.tvFlight.setText(notif.getFlight());

        boolean opened = notif.isOpened();
        if(!opened) holder.parentLayout.setBackgroundResource(R.color.sky_blue);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotifBodyFragment frag = new NotifBodyFragment();
                Activity act = (Activity) mContext;
                new UserSelectedNotif(notif);
                openNotif(notif);
                act.getFragmentManager().beginTransaction().replace(R.id.empty_container,frag).addToBackStack(null).commit();
            }
        });


        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                mBuilder.setTitle("Delete");
                mBuilder.setMessage("Do you want to delete this notification?");
                mBuilder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();



                            deleteNotif(notif);



                    }
                });
                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                AlertDialog alertDialog = mBuilder.create();
                alertDialog.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    class NotifViewHolder extends RecyclerView.ViewHolder{
        LinearLayout parentLayout;
        TextView tvFlight,tvStatus;
        public NotifViewHolder(View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            tvFlight = itemView.findViewById(R.id.tvFlight);
            tvStatus = itemView.findViewById(R.id.tvStats);

        }
    }

    private void openNotif(Notif notif){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String id = FirebaseAuth.getInstance().getUid();
        DatabaseReference ref = db.getReference("Users/" +id+"/Notification/"+notif.getUid()+"/opened");
        ref.setValue(true);
    }

    private void deleteNotif(Notif notif){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String id = FirebaseAuth.getInstance().getUid();
        DatabaseReference ref = db.getReference("Users/" +id+"/Notification/"+notif.getUid());
        ref.removeValue();
    }
}

