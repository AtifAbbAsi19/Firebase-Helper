package inc.droidflick.firebasetutorial.interfaces;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;

/**
 * Created by Atif Arif on 12/29/2017.
 */

public interface FireBaseOnTaskComplete {
    public void onComplete(@NonNull Task<Void> task);
}
