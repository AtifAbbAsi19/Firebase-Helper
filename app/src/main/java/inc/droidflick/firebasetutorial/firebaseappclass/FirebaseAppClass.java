package inc.droidflick.firebasetutorial.firebaseappclass;

import android.app.Application;

import com.google.firebase.FirebaseApp;

import inc.droidflick.firebasetutorial.firebasenetwork.FireBaseHelper;

/**
 * Created by Atif Arif on 12/18/2017.
 */

public class FirebaseAppClass extends Application {

    FireBaseHelper fireBaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        fireBaseHelper.initFireBase();
    }

}
