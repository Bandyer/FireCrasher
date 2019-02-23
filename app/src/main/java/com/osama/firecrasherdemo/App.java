package com.osama.firecrasherdemo;

import android.app.Activity;
import android.app.Application;
import android.widget.Toast;

import com.osama.firecrasher.CrashListener;
import com.osama.firecrasher.FireCrasher;

/**
 * Created by Osama Raddad.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        FireCrasher.INSTANCE.install(this, new CrashListener() {

            @Override
            public void onCrash(Throwable throwable, final Activity activity) {
                Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();

//                start the recovering process
                recover(activity);

                //you need to add your crash reporting tool here
                //Ex: Crashlytics.logException(throwable);
            }
        });
        super.onCreate();
    }
}
