package com.osama.firecrasherdemo

import android.app.AlertDialog
import android.app.Application
import android.app.ProgressDialog
import android.widget.Toast
import com.osama.firecrasher.CrashLevel
import com.osama.firecrasher.CrashListener
import com.osama.firecrasher.FireCrasher


/**
 * Created by Osama Raddad.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FireCrasher.install(this, object : CrashListener() {

            override fun onCrash(throwable: Throwable) {

                FireCrasher.evaluateAsync { activity, crashLevel ->
                    if (FireCrasher.retryCount <= 1 && crashLevel == CrashLevel.LEVEL_ONE) {
                        val pd = ProgressDialog(activity)
                        pd.setMessage("loading")
                        pd.show()
                        FireCrasher.recover {
                            Toast.makeText(this@App, "recover", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        val title = when (crashLevel) {
                            CrashLevel.LEVEL_ONE -> "Crash Level One"
                            CrashLevel.LEVEL_TWO -> "Crash Level Two"
                            CrashLevel.LEVEL_THREE -> "Crash Level Three"
                        }

                        val positiveButtonText = when (crashLevel) {
                            CrashLevel.LEVEL_ONE -> "Retry"
                            CrashLevel.LEVEL_TWO -> "Go Back"
                            CrashLevel.LEVEL_THREE -> "Restart The App"
                        }
                        val builder = AlertDialog.Builder(activity)
                        builder.setTitle(title)
                        builder.setMessage(throwable.localizedMessage)
                        builder.setPositiveButton(positiveButtonText) { _, _ ->
                            FireCrasher.recover {
                                Toast.makeText(this@App, "recover", Toast.LENGTH_LONG).show()
                            }
                        }
                        if (crashLevel != CrashLevel.LEVEL_THREE)
                            builder.setNegativeButton("Restart The App") { _, _ ->
                                FireCrasher.recover(CrashLevel.LEVEL_THREE) {
                                    Toast.makeText(this@App, "recover", Toast.LENGTH_LONG).show()
                                }
                            }
                        builder.setIcon(android.R.drawable.ic_dialog_alert)
                        builder.show()
                    }
                }


                //you need to add your crash reporting tool here
                //Ex: Crashlytics.logException(throwable);
            }
        })
    }
}
