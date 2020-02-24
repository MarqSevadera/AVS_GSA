package dev.philsca_capstone.avs_gsa.SelectedInstances;

import dev.philsca_capstone.avs_gsa.Models.Flight;

public class UserSelectedFlight {
    private static Flight selectedFlight = null;

    public UserSelectedFlight(Flight flight){
        selectedFlight = flight;
    }

    public static Flight getSelectedFlight(){
        return selectedFlight;
    }

    public static void resetFlight(){
        selectedFlight = null;
    }
}

