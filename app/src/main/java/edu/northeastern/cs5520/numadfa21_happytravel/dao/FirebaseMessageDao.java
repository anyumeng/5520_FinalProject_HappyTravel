package edu.northeastern.cs5520.numadfa21_happytravel.dao;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



/**
 * A firebase realtime database dao.
 */
public class FirebaseMessageDao implements MessageDao {
    public static final int STICKER_NUM = 4;
    private static final String TAG = "DAO";
    private boolean flag = false;

    /**
     * The message dao using the root reference.
     */
    public FirebaseMessageDao() {}

//    @Override
//    public void readHistory() {
//        DatabaseReference testReference =
//                FirebaseDatabase.getInstance()
//                        .getReference(User.class.getSimpleName());
//        HashMap<String, User> userInfo = new HashMap<>();
//        testReference.orderByKey().addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    User user = snapshot.getValue(User.class);
//                    user.setKey(snapshot.getKey());
//                    userInfo.put(user.getUserName(), user);
//                }
//                return;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    @Override
    public String getToken(String userName) {
        return null;
    }

//    @Override
//    public HashMap<String, User> newUser(String userName, Context context) {
//        HashMap<String, User> userInfo = new HashMap<>();
//        FirebaseMessaging.getInstance()
//                .getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Toast.makeText(context, "Fail to getToken()",
//                                    Toast.LENGTH_LONG)
//                                    .show();
//                        } else {
//                            String newToken = task.getResult();
//                            User newUser = new User(userName, newToken);
//                            newUser.setKey("");
//                            ArrayList sendInfo = new ArrayList(STICKER_NUM);
//                            ArrayList receiveNum = new ArrayList(STICKER_NUM);
//                            ArrayList receiveInfo = new ArrayList<ArrayList<Pair<String,
//                                    String>>>();
//                            for (int i = 0; i < STICKER_NUM; i++) {
//                                sendInfo.add(0);
//                                receiveNum.add(0);
//                            }
//                            newUser.setSendInfo(sendInfo);
//                            newUser.setReceiveNum(receiveNum);
//                            newUser.setReceiveInfo(receiveInfo);
//                            //                    Log.d("MainActivity", newToken);
//                            MessageDao.writeToDatabase(User.class.getSimpleName(),
//                                    newUser, context);
//                            for(Map.Entry<String, User> newUserInfo: readHistory().entrySet()) {
//                                userInfo.put(newUserInfo.getKey(), newUserInfo.getValue());
//                            }
//                        }
//                    }
//                });
//        return userInfo;
//    }

    /*
     @Override
    public void updateToken(String userName, Context context) {
        FirebaseMessaging.getInstance()
                .getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, "Fail to getToken()",
                                    Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            String newToken = task.getResult();

                            DatabaseReference testReference =
                                    FirebaseDatabase.getInstance()
                                            .getReference();

                            testReference.orderByKey().addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        User user = snapshot.getValue(User.class);
                                        if (user.getUserName().equals(userName)) {
                                            user.setKey(snapshot.getKey());
                                            String key = snapshot.getKey();
                                            testReference.child(key).child("token").setValue(newToken);
                                            Toast.makeText(context, "Successfully update token",
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                            break;
                                        }
                                    }
                                    return;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                });
    }

     */

    @Override
    public boolean isExist(String dbName, String key) {
        DatabaseReference testReference =
                FirebaseDatabase.getInstance()
                        .getReference(dbName);

        testReference.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    /*
                    User currentUser = snapshot.getValue(User.class);
                    Log.d(TAG, currentUser.getUserName() + " " + key);

                    if (currentUser.getUserName().equals(key) || currentUser.getEmail().equals(key)) {
                        flag = true;
                    }
                    */

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return flag;
    }


}
