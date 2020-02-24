package dev.philsca_capstone.avs_gsa.SelectedInstances;

import dev.philsca_capstone.avs_gsa.Models.Airline;

public class UserSelectedAirline {
    private static Airline selectedAirline = null;

    public UserSelectedAirline(Airline airline){
        selectedAirline = airline;
    }

    public static Airline getSelectedAirline(){
        return selectedAirline;
    }

    public static void resetAirline(){
        selectedAirline = null;
    }
}
