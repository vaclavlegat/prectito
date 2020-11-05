package cz.legat.prectito

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

const val LIFECYCLE_TAG = "LIFECYCLE"

@HiltAndroidApp
class PrectitoApp: Application(), Application.ActivityLifecycleCallbacks{

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        Log.d(LIFECYCLE_TAG, "onActivityCreated:" + activity.localClassName)
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d(LIFECYCLE_TAG, "onActivityDestroyed:" + activity.localClassName)
    }

    override fun onActivityPaused(activity: Activity) {
        Log.d(LIFECYCLE_TAG, "onActivityPaused:" + activity.localClassName)
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d(LIFECYCLE_TAG, "onActivityResumed:" + activity.localClassName)
    }

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle?
    ) {
        Log.d(LIFECYCLE_TAG, "onActivitySaveInstanceState:" + activity.localClassName)
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d(LIFECYCLE_TAG, "onActivityStarted:" + activity.localClassName)
    }

    override fun onActivityStopped(activity: Activity) {
        Log.d(LIFECYCLE_TAG, "onActivityStopped:" + activity.localClassName)
    }
}