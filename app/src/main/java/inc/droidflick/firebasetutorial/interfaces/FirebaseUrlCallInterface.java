package inc.droidflick.firebasetutorial.interfaces;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by Atif Arif on 12/29/2017.
 */

public interface FirebaseUrlCallInterface {

    public void onFirebaseCallComplete(String url);

    public void onFirebaseCallFailure(@NonNull Exception exception);

}
