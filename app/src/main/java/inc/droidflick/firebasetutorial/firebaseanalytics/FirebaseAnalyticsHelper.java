package inc.droidflick.firebasetutorial.firebaseanalytics;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Atif Arif on 1/2/2018.
 */

public class FirebaseAnalyticsHelper {


    FirebaseAnalytics mFirebaseAnalytics;

    public static FirebaseAnalyticsHelper firebaseAnalyticsHelper;

    Context context;


    public FirebaseAnalyticsHelper(Context context) {
        this.context = context;
    }


    public static FirebaseAnalyticsHelper getInstance(Context context) {

        if (firebaseAnalyticsHelper == null) {
            return firebaseAnalyticsHelper = new FirebaseAnalyticsHelper(context);
        } else {
            return firebaseAnalyticsHelper;
        }


    }


    public void addLogEvents(String eventType, Bundle params) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mFirebaseAnalytics.logEvent(eventType, params);


    }


}
