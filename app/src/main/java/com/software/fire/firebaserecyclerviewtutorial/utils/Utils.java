package com.software.fire.firebaserecyclerviewtutorial.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Brad on 12/15/2016.
 */

public class Utils {
    public static String getUID() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.POSTS).push();
        String[] urlArray = databaseReference.toString().split("/");
        return urlArray[urlArray.length - 1];
    }
}
