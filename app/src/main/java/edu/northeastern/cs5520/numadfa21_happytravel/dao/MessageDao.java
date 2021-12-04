package edu.northeastern.cs5520.numadfa21_happytravel.dao;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * A series of functions that connects this app to firebase realtime database.
 */
public interface MessageDao {

    /**
     * Read whole message history for an user.
     *
     * @return the list of all messages that all users received and sent.
     */
//    public HashMap<String, User> readHistory();

    /**
     * Get the sticker stats for an user.
     * @param userName the user name.
     * @return the token for the user.
     */
//    public List<StickerStats> getStats(String userName);

    /**
     * Get token based on an user name.
     * @param userName the user id to get token.
     * @return the token for the specific user.
     */
    public String getToken(String userName);

    /**
     * Register a new user to the database with their token
     *
     * @param userName user name of this user.
     * @param context the context to do UI change.
     * @return the new HashMap with the new user included.
     */
//    public HashMap<String, User> newUser(String userName, Context context);

    /**
     * Update the token for an existing user to bind to current device
     *
     * @param userName user name of this user.
     * @param context the context to do UI change.
     */
//    public void updateToken(String userName, Context context);

    /**
     * A simple function to write to database with a new message with fixed from, to, sticker_id
     * and time.
     *
     * @param dbName name of db: Message or User
     * @param obj    an object need to be written
     */
    public static void writeToDatabase(String dbName, Object obj, Context context) { //Shutong: we can pass in a string
        // to represent with db
        DatabaseReference testReference =
                FirebaseDatabase.getInstance()
                        .getReference(dbName);
        //Shutong: Use for test
        testReference.push()
                .setValue(obj)
                .addOnSuccessListener(unused -> Toast.makeText(context, "Success " +
                        "to count message", Toast.LENGTH_SHORT)
                        .show())
                .addOnFailureListener(e -> Toast.makeText(context, "Fail to count " +
                        "message", Toast.LENGTH_SHORT)
                        .show());


    }

    /**
     * When sending and receiving message, update the sender and user info into database
     *
     * @param dbName  name of database: Message or User
     * @param key     key of one object in db, i.e.MnrMM7xk8dXSmHaBvrX
     * @param hashMap update in the form of hashMap
     * @param context the context to use for UI changes.
     */
    public static void updateToDatabase(String dbName, String key, HashMap<String, Object> hashMap, Context context) {
        DatabaseReference testReference =
                FirebaseDatabase.getInstance()
                        .getReference(dbName);
        testReference.child(key)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Success to update user",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Fail to update user",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    public boolean isExist(String dbName, String key);


}
