package inc.droidflick.firebasetutorial.firebasenetwork;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import inc.droidflick.firebasetutorial.R;
import inc.droidflick.firebasetutorial.interfaces.FirebaseCallInterface;

import java.util.*;

/**
 * Created by Atif Arif on 12/18/2017.
 */

public class FireBaseHelper {


    Context context;
    FirebaseCallInterface firebaseCallInterface;

    String imageUrl = "ic_default";

    public static int childCount = -1;

    public static FireBaseHelper fireBaseHelper;

    public static FirebaseAuth mAuth;

    public static FirebaseDatabase mDatabase;

    public static DatabaseReference mDatabaseReference;

    public static StorageReference mFStorage;

    public static StorageReference filePath;

    public static ProgressDialog progressDialog;


    public static Uri galleryUri;

    public static Uri audioUri;

    public static String imageDownloadUrl = null;

    public static String audioDownloadUrl = null;

    public static String LOG_TAG = null;


    public static boolean imageFlag = false;
    public static boolean audioFlag = false;

    public FireBaseHelper(Context context, FirebaseCallInterface firebaseCallInterface) {
        this.context = context;
        this.firebaseCallInterface = firebaseCallInterface;
    }


    //query.orderByChild("date").startAt(new DateTime().getMillis())

    public static FireBaseHelper getInstance(Context context, FirebaseCallInterface firebaseCallInterface) {

        if (fireBaseHelper == null) {
            return fireBaseHelper = new FireBaseHelper(context, firebaseCallInterface);
        } else {
            return fireBaseHelper;
        }


    }

    public static void initFireBase() {

        //initializing firebase auth object
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
        mFStorage = FirebaseStorage.getInstance().getReference();
//        mDatabase.getReference().keepSynced(false);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
//        mDatabase.getReference().keepSynced(false);


    }


    public static StorageReference getStorageReference() {
        mFStorage = FirebaseStorage.getInstance().getReference();
        return mFStorage;
    }

    public static DatabaseReference getDataBaseRefrence() {
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
        return mDatabaseReference;
    }


    public static <T> ArrayList<T> readSnapShot(DataSnapshot dataSnapshot,
                                                Class<T> clazz) {

//         ArrayList<Clazz> tempArray=new ArrayList<>();

        ArrayList<T> arrayList = new ArrayList<>();

        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

            // key for node .!
            // String key = childSnapshot.getKey();

            T object = (T) childSnapshot.getValue(clazz);

            arrayList.add(object);

        }

        return arrayList;
    }

    public static <T> ArrayList<T> readSnapShotKey(DataSnapshot dataSnapshot,
                                                   ArrayList<T> arrayList, Class<?> clazz) {

        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            String key = childSnapshot.getKey();

            T object = (T) childSnapshot.getValue(clazz);

            arrayList.add(object);

        }

        return arrayList;
    }


    public static String getRandomId() {

        String id = UUID.randomUUID().toString();
        return id;
    }


    public static String getCurrentUserId() {
        return mAuth.getCurrentUser().getUid();
    }


    public static String getCurrentUserEmail() {
        return mAuth.getCurrentUser().getEmail();
    }


    public static int getChildCount(DatabaseReference mDatabaseReference, Context context) {


        if (isNetworkAvailable(context) && mDatabaseReference != null) {

            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    childCount = Integer.valueOf((int) dataSnapshot.getChildrenCount());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection.!", Toast.LENGTH_SHORT).show();
        }


        return childCount;
    }


    public void addValueEventListener(DatabaseReference mDatabaseRef, Context context) {


        if (isNetworkAvailable(context) && mDatabaseRef != null) {

            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    firebaseCallInterface.onFirebaseCallComplete(dataSnapshot);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    firebaseCallInterface.onFirebaseCallFailure(databaseError);

                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection.!", Toast.LENGTH_SHORT).show();
        }


    }

    public void addListenerForSingleValueEvent(DatabaseReference mDatabaseRef) {

        if (context != null && isNetworkAvailable(context) && mDatabaseRef != null) {

            mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    firebaseCallInterface.onFirebaseCallComplete(dataSnapshot);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    firebaseCallInterface.onFirebaseCallFailure(databaseError);

                }
            });
        } else {
            if (context != null) {
                Toast.makeText(this.context, "No Internet Connection.!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.context, "Unable To Detect Context.!", Toast.LENGTH_SHORT).show();

            }

        }


    }


    public String uploadImage(StorageReference filePath, Uri uri) {


        progressDialog.setMessage(context.getString(R.string.loading));
        progressDialog.show();


//        filePath = mFStorage.child("image").child(getRandomId() + ".png");

        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                progressDialog.dismiss();

                imageUrl = taskSnapshot.getDownloadUrl().toString();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.dismiss();
                //Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                //displaying the upload progress
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
            }
        });


        return imageUrl;
    }


    public void uploadAudio(Uri uri, Context context) {

        progressDialog.setMessage(context.getString(R.string.loading));
        progressDialog.show();

        // Create file metadata including the content type
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("audio/mpeg")
                .build();


        filePath = mFStorage.child("audio").child(getRandomId() + ".mp3");

        // Uri uri = Uri.fromFile(new File(mFileName));

        filePath.putFile(galleryUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                progressDialog.dismiss();

                galleryUri = taskSnapshot.getDownloadUrl();

                imageFlag = true;
                ;

            }
        });
    }


    public static void deleteFirebaseUserAuthAccount(Context context, String email,
                                                     String password) {


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(LOG_TAG, "User account deleted.");
                                        }
                                    }
                                });

                    }
                });
    }


    public static void deleteUserAuthAccount(Context context) {

        Log.d(LOG_TAG, "deleteAccount");

        final FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();
        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "OK! Works fine!");
                    // startActivity(new Intent(Main3WelcomeActivity.this, Main3Activity.class));
                    //  finish();
                } else {
                    Log.w(LOG_TAG, "Something is wrong!");
                }
            }
        });
    }


    public static void deleteDatabaseNode(DatabaseReference dbNode, final Activity activity) {


//        dbNode.setValue(null, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//
//            }
//        });

        dbNode.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Unable to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public static void deleteDatabaseChildNode(DatabaseReference mdatabaseReference,String nodeName) {

//        mdatabaseReference.orderByKey().equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot postsnapshot :dataSnapshot.getChildren()) {
//
//                    String key = postsnapshot.getKey();
//                    dataSnapshot.getRef().removeValue();
//
//                }

//    }


    public static void showDialog(Context context, String message) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public static void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // test for connection
            if (cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                    && cm.getActiveNetworkInfo().isConnected()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
        }
        return false;
    }

}



