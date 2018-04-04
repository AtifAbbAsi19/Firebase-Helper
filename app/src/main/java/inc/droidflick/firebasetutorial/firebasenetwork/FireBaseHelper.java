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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
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
import inc.droidflick.firebasetutorial.interfaces.FireBaseChildCountCallInterface;
import inc.droidflick.firebasetutorial.interfaces.FireBaseOnTaskComplete;
import inc.droidflick.firebasetutorial.interfaces.FirebaseCallInterface;
import inc.droidflick.firebasetutorial.interfaces.FirebaseUrlCallInterface;

import java.util.*;
import java.util.concurrent.Executor;

/**
 * Created by Atif Arif on 12/18/2017.
 */

public class FireBaseHelper {


    Context context;


    FirebaseCallInterface firebaseCallInterface;
    FireBaseChildCountCallInterface childCountCallInterface;
    FirebaseUrlCallInterface firebaseUrlCallInterface;
    FirebaseUrlCallInterface firebaseAudioUrlCallInterface;
    FireBaseOnTaskComplete onTaskCompleteInterface;
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

//    public static String imageDownloadUrl = null;
//
//    public static String audioDownloadUrl = null;
//
//    public static String LOG_TAG = null;
//
//
//    public static boolean imageFlag = false;
//    public static boolean audioFlag = false;

    public FireBaseHelper(Context context) {
        this.context = context;
    }


    //query.orderByChild("date").startAt(new DateTime().getMillis())

    public static FireBaseHelper getInstance(Context context) {

        if (fireBaseHelper == null) {
            return fireBaseHelper = new FireBaseHelper(context);
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


    private void registerUser(final String email, String password,Context context) {


//            if (TextUtils.isEmpty(phone_number)) {
//                Toast.makeText(this, "Please enter Number", Toast.LENGTH_LONG).show();
//                return;
//            }


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        //if the email and password are not empty
        //displaying a progress dialog

//        progressDialog.setMessage("Registering Please Wait...");
//        progressDialog.show();

///http://stackoverflow.com/questions/40404567/how-to-send-verification-email-with-firebase


//         .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
//         .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {


        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            //display some message here

                            String userId = task.getResult().getUser().getUid();


//                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                            startActivity(intent);

//https://firebase.googleblog.com/2017/02/email-verification-in-firebase-auth.html

                            //  startActivity(new Intent(Signup_Activity.this, MainHomeDashBoard.class));
//                            Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_LONG).show();
                        } else {


                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
//                                Toast.makeText(Signup.this, "User with this email already exist.", Toast.LENGTH_SHORT).show();
                            }
//

//                            //display some message here
//                            Toast.makeText(getApplicationContext(), "Registration Error", Toast.LENGTH_LONG).show();
                        }
//                        progressDialog.dismiss();
                    }
                });

    }


    //method for user login
    private void userLogin(String email,String password,Context context) {



        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Logging in  Please Wait...");
        progressDialog.show();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //if the task is successfull
                        if (task.isSuccessful()) {
                            //start the profile activity

                            //Check in user Node that whether data exists or not
                            //if exists then login else show Snakbar data does not exist

//                            mAuthUserStr = mAuth.getCurrentUser().getUid();
//                            mEmail = mAuth.getCurrentUser().getEmail();

//                            AddEventFireBaseListner(mAuthUserStr, TypeSpinnerStr);

                            //  startActivity(new Intent(getApplicationContext(), MainHomeDashBoard.class));
                        } else {

//                            Snackbar snackbar = Snackbar
//                                    .make(coordinatorLayout, "Invalid user", Snackbar.LENGTH_LONG)
//                                    .setAction("HIDE", new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//
//
//                                        }
//                                    });
//                            snackbar.show();


                            progressDialog.dismiss();


                        }
                    }


                });

    }//end of user login


    public String getCurrentUserId() {
        return mAuth.getCurrentUser().getUid();
    }


    public String getCurrentUserEmail() {
        return mAuth.getCurrentUser().getEmail();
    }


    public void getChildCount(DatabaseReference mDatabaseReference,
                              FireBaseChildCountCallInterface childTotalCountInterface) {

        if (isNetworkAvailable(context) && mDatabaseReference != null
                && childTotalCountInterface != null) {


            this.childCountCallInterface = childTotalCountInterface;

            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    childCount = Integer.valueOf((int) dataSnapshot.getChildrenCount());
                    childCountCallInterface.onFirebaseCallComplete(childCount);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    childCountCallInterface.onFirebaseCallFailure(databaseError);
                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection.!", Toast.LENGTH_SHORT).show();
        }

    }


    public void addValueEventListener(DatabaseReference mDatabaseRef, FirebaseCallInterface dataCallInterface) {


        if (isNetworkAvailable(context) && mDatabaseRef != null && dataCallInterface != null) {

            this.firebaseCallInterface = dataCallInterface;

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
            if (context != null) {
                Toast.makeText(this.context, "No Internet Connection.!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.context, "Unable To Detect Context.!", Toast.LENGTH_SHORT).show();

            }

        }

    }

    public void addListenerForSingleValueEvent(DatabaseReference mDatabaseRef,
                                               FirebaseCallInterface dataCallInterface) {


        if (context != null && isNetworkAvailable(context) && mDatabaseRef != null &&
                dataCallInterface != null) {

            this.firebaseCallInterface = dataCallInterface;

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


    public void uploadImage(StorageReference filePath, Uri uri, FirebaseUrlCallInterface urlCallInterface) {


        this.firebaseUrlCallInterface = urlCallInterface;

        progressDialog.setMessage(context.getString(R.string.loading));
        progressDialog.show();


//        filePath = mFStorage.child("image").child(getRandomId() + ".png");

        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                progressDialog.dismiss();

                String imageUrl = taskSnapshot.getDownloadUrl().toString();

                firebaseUrlCallInterface.onFirebaseCallComplete(imageUrl);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                progressDialog.dismiss();
                firebaseUrlCallInterface.onFirebaseCallFailure(exception);

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


//        return imageUrl;
    }


    public void uploadAudio(Uri uri, StorageReference filePath, FirebaseUrlCallInterface urlAudioCallInterface) {


        this.firebaseAudioUrlCallInterface = urlAudioCallInterface;

        progressDialog.setMessage(context.getString(R.string.loading));
        progressDialog.show();

        // Create file metadata including the content type
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("audio/mpeg")
                .build();


        filePath = mFStorage.child("audio").child(getRandomId() + ".mp3");

        filePath.putFile(galleryUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                progressDialog.dismiss();
                String url = taskSnapshot.getDownloadUrl().toString();
                firebaseAudioUrlCallInterface.onFirebaseCallComplete(url);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                progressDialog.dismiss();
                firebaseAudioUrlCallInterface.onFirebaseCallFailure(exception);

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
                                            // Log.d(LOG_TAG, "User account deleted.");
                                        }
                                    }
                                });

                    }
                });
    }


    public void deleteUserAuthAccount(FireBaseOnTaskComplete onTaskComplete) {

        // Log.d(LOG_TAG, "deleteAccount");

        if (onTaskComplete != null) {

            this.onTaskCompleteInterface = onTaskComplete;


            final FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();
            currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    onTaskCompleteInterface.onComplete(task);
                    if (task.isSuccessful()) {


                        // Log.d(LOG_TAG, "OK! Works fine!");
                        // startActivity(new Intent(Main3WelcomeActivity.this, Main3Activity.class));
                        //  finish();
                    } else {
                        //  Log.w(LOG_TAG, "Something is wrong!");
                    }
                }
            });


        }

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



