package dev.philsca_capstone.avs_gsa.SelectedInstances;

import dev.philsca_capstone.avs_gsa.Models.Reservation;

public class UserSelectedReservation {
    private static Reservation selectedReservation = null;

    public UserSelectedReservation(Reservation reservation){
        selectedReservation = reservation;
    }

    public static Reservation getSelectedReservation(){
        return selectedReservation;
    }

    public static void resetReservation(){
        selectedReservation = null;
    }
}
