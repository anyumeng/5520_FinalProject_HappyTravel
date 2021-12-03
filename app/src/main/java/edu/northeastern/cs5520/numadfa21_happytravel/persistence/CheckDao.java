package edu.northeastern.cs5520.numadfa21_happytravel.persistence;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CheckDao {
    public static void simpleWrite() {
        DatabaseReference testReference = FirebaseDatabase.getInstance().getReference("test");
        testReference.child("fake data").setValue("fake fake");
    }

    public static void simpleRead() {
        DatabaseReference testReference = FirebaseDatabase.getInstance().getReference("test");
        testReference
                .child("fake data")
                .get()
                .addOnCompleteListener(
                        task -> {
                            DataSnapshot snapshot = task.getResult();
                            Log.v(
                                    "TEST",
                                    String.format("the test value is: %s", snapshot.getValue()));
                        });
    }
}
