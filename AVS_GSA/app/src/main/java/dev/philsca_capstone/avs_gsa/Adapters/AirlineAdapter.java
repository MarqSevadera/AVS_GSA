package dev.philsca_capstone.avs_gsa.Adapters;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import dev.philsca_capstone.avs_gsa.Enum.Action;
import dev.philsca_capstone.avs_gsa.Fragments.NewReservationFrament;
import dev.philsca_capstone.avs_gsa.Fragments.SelectFlight;
import dev.philsca_capstone.avs_gsa.Models.Airline;
import dev.philsca_capstone.avs_gsa.R;
import dev.philsca_capstone.avs_gsa.SelectedInstances.UserSelectedAction;
import dev.philsca_capstone.avs_gsa.SelectedInstances.UserSelectedAirline;

public class AirlineAdapter extends RecyclerView.Adapter<AirlineAdapter.AirlineViewHolder>{


    private Context mContext;
    private List<Airline> airlineList;

    public AirlineAdapter(Context mContext, List<Airline> airlineList) {
        this.mContext = mContext;
        this.airlineList = airlineList;
    }


    @NonNull
    @Override
    public AirlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_airline,null);

        return new AirlineViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AirlineViewHolder holder, int position) {
        final Airline airline = airlineList.get(position);
        holder.tvName.setText(airline.getName());
        Glide.with(mContext).load(airline.getImage()).into(holder.imageView);
        holder.parentLayout.setOnClickListener(new RecyclerView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag;
                Activity act = (Activity) mContext;
                new UserSelectedAirline(airline);
                Action action = UserSelectedAction.getSelectedAction();

                if(action == Action.RESERVATION){
                    frag = new NewReservationFrament();
                }else{
                    frag = new SelectFlight();
                }

                act.getFragmentManager().beginTransaction().replace(R.id.empty_container,frag).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return airlineList.size();
    }

    class AirlineViewHolder extends RecyclerView.ViewHolder{
        LinearLayout parentLayout;
        ImageView imageView;
        TextView tvName,tvDesc;
        public AirlineViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            tvName = itemView.findViewById(R.id.textViewTitle);
            tvDesc = itemView.findViewById(R.id.textViewShortDesc);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
