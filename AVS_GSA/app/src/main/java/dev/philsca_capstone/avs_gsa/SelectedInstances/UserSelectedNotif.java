package dev.philsca_capstone.avs_gsa.SelectedInstances;

import dev.philsca_capstone.avs_gsa.Models.Notif;

public class UserSelectedNotif {
    private static Notif selectedNotif = null;

    public UserSelectedNotif(Notif notif){
        selectedNotif = notif;
    }

    public static Notif getSelectedNotif(){
        return selectedNotif;
    }

    public static void resetNotif(){
        selectedNotif = null;
    }
}
