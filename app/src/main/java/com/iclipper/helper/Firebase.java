package com.iclipper.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Erico on 14/02/2018.
 */

public class Firebase {

    private static DatabaseReference databaseReference;
    private static FirebaseAuth firebaseAuth;
    private static String userId;
    private Preferences preferences;









    public static DatabaseReference getFirebase(){

        if(databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference().child("traducoes").child("Usuario");
        }
        return databaseReference;
    }

    public static FirebaseAuth getFirebaseAuth(){
       if(firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
       }
        return firebaseAuth;
    }

    public static void logOut(){
        if(firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
        }else{
            firebaseAuth.signOut();

        }

    }

    public static void loginDatabase(){
        if(databaseReference != null){
            databaseReference = FirebaseDatabase.getInstance().getReference().child("traducoes").child("Usuario");
        }else {
            try {



                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            }catch (Exception e){
                e.printStackTrace();

            }
         //   databaseReference = FirebaseDatabase.getInstance().getReference().child("traducoes").child("Usuario").child(getUserId());

        }
    }
    public static String getUserId() {
        return userId;
    }


}
