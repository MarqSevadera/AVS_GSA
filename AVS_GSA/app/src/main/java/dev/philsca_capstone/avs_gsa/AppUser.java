package dev.philsca_capstone.avs_gsa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dev.philsca_capstone.avs_gsa.Models.User;

public class AppUser {
    private static User firstInstance = null;

    private AppUser(){}

    public static User getInstance() {
        if (firstInstance == null) {
            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users");

            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                       firstInstance = dataSnapshot.child(uid).getValue(User.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{

        }
        return firstInstance;
    }

    public static void logoutUser(){
        firstInstance = null;
    }

}
