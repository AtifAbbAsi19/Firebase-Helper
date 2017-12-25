package inc.droidflick.firebasetutorial.interfaces;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by Atif Arif on 12/18/2017.
 */

public interface FirebaseCallInterface {


    public void onFirebaseCallComplete(DataSnapshot dataSnapshot);

    public void onFirebaseCallFailure(DatabaseError databaseError);


}
