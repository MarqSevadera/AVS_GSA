package dev.philsca_capstone.avs_gsa.Adapters;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import dev.philsca_capstone.avs_gsa.Activities.Home;
import dev.philsca_capstone.avs_gsa.Fragments.Tracking;
import dev.philsca_capstone.avs_gsa.Models.Flight;
import dev.philsca_capstone.avs_gsa.R;
import dev.philsca_capstone.avs_gsa.SelectedInstances.UserSelectedAirline;
import dev.philsca_capstone.avs_gsa.SelectedInstances.UserSelectedFlight;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder>{


    private Context mContext;
    private List<Flight> flightList;

    public FlightAdapter(Context context, List<Flight> flightList) {
        this.mContext = context;
        this.flightList = flightList;
    }


    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_flight,null);

        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        final Flight flight = flightList.get(position);
        holder.tvCode.setText(flight.getCode());
        holder.tvAirline.setText(UserSelectedAirline.getSelectedAirline().getName());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag = new Tracking();
                Activity act = (Activity) mContext;
                new UserSelectedFlight(flight);
                act.getFragmentManager().beginTransaction().replace(R.id.empty_container,frag).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    class FlightViewHolder extends RecyclerView.ViewHolder{
        TextView tvCode , tvAirline;
        LinearLayout parentLayout;
        private FlightViewHolder(View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tvCode);
            tvAirline = itemView.findViewById(R.id.tvAirline);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
