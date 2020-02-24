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

import dev.philsca_capstone.avs_gsa.Activities.Home;
import dev.philsca_capstone.avs_gsa.Fragments.DetailsFragment;
import dev.philsca_capstone.avs_gsa.Fragments.Tracking;
import dev.philsca_capstone.avs_gsa.Models.Flight;
import dev.philsca_capstone.avs_gsa.Models.Reservation;
import dev.philsca_capstone.avs_gsa.R;
import dev.philsca_capstone.avs_gsa.SelectedInstances.UserSelectedAirline;
import dev.philsca_capstone.avs_gsa.SelectedInstances.UserSelectedFlight;
import dev.philsca_capstone.avs_gsa.SelectedInstances.UserSelectedReservation;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>{
    private Context mContext;
    private List<Reservation> reservationList;

    public ReservationAdapter(Context context, List<Reservation> reservationList) {
        this.mContext = context;
        this.reservationList = reservationList;
    }


    @NonNull
    @Override
    public ReservationAdapter.ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_reservation,parent,false);
        return new ReservationAdapter.ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationAdapter.ReservationViewHolder holder, int position) {
        final Reservation reservation = reservationList.get(position);
        holder.tvDestination.setText(reservation.getOriginCode() + "-" + reservation.getDestinationCode());
        holder.tvAirline.setText(reservation.getAirline());
        String status = reservation.getStatus();
        holder.tvStatus.setText(status);

        final Activity act = (Activity) mContext;
        if(TextUtils.equals("Accepted" , status) || TextUtils.equals("Shipped" , status)){
            holder.btnTrack.setEnabled(true);
            holder.btnTrack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Flight flight = new Flight(reservation.getFlightCode());
                    new UserSelectedFlight(flight);
                    Tracking tracking = new Tracking();
                    act.getFragmentManager().beginTransaction().replace(R.id.empty_container,tracking).addToBackStack(null).commit();
                }
            });
        }
        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UserSelectedReservation(reservation);
                DetailsFragment details = new DetailsFragment();
                act.getFragmentManager().beginTransaction().replace(R.id.empty_container,details).addToBackStack(null).commit();
            }
        });

        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                mBuilder.setTitle("Delete");
                mBuilder.setMessage("Do you want to delete this reservation?");
                mBuilder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(!TextUtils.equals(reservation.getStatus(),"Arrived") && !TextUtils.equals(reservation.getStatus(),"Rejected")){
                            Toast.makeText(mContext,"You cannot delete this yet, since the cargo has not yet arrived!",Toast.LENGTH_SHORT).show();
                        }else{
                            deleteReservation(reservation);
                        }

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
        return reservationList.size();
    }

    class ReservationViewHolder extends RecyclerView.ViewHolder{
        TextView tvDestination , tvAirline , tvStatus, btnTrack , btnDetails;
        LinearLayout parentLayout;
        private ReservationViewHolder(View itemView) {
            super(itemView);
            tvDestination= itemView.findViewById(R.id.tvDestination);
            tvAirline = itemView.findViewById(R.id.tvAirline);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnDetails = itemView.findViewById(R.id.btnDetails);
            btnTrack = itemView.findViewById(R.id.btnCardTrack);
            parentLayout = itemView.findViewById(R.id.reservationCard);
        }
    }


    private void deleteReservation(Reservation reservation){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref  = db.getReference("Users/"+reservation.getUserId()+"/Reservation/"+reservation.getUid());
        ref.removeValue();
    }
}
