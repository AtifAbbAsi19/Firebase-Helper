package inc.droidflick.firebasetutorial.interfaces;

import com.google.firebase.database.DatabaseError;

/**
 * Created by Atif Arif on 12/29/2017.
 */

public interface FireBaseChildCountCallInterface {

    public void onFirebaseCallComplete(int childCount);

    public void onFirebaseCallFailure(DatabaseError databaseError);

}
