package dev.philsca_capstone.avs_gsa;


import dev.philsca_capstone.avs_gsa.Enum.Action;

public class UserAction {

    private static Action mAction = null;

    public UserAction(Action action){
        mAction = action;
    }

    public static Action getAction(){
        return mAction;
    }

    public static void resetAction(){
        mAction = null;
    }
}
