import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by kiran on 9/29/2017.
 */
public class myapplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);

    }
}
