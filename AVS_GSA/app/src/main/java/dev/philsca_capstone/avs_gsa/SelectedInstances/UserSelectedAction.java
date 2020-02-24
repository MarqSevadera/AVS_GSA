package dev.philsca_capstone.avs_gsa.SelectedInstances;

import dev.philsca_capstone.avs_gsa.Enum.Action;

public class UserSelectedAction {
    private static Action selectedAction = null;

    public UserSelectedAction(Action action){
        selectedAction = action;
    }

    public static Action getSelectedAction(){
        return selectedAction;
    }

    public static void resetSelectedAction(){
        selectedAction = null;
    }
}
