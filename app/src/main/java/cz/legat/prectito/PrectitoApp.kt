package cz.legat.prectito

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

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
        Timber.d("onActivityCreated")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Timber.d("onActivityDestroyed")
    }

    override fun onActivityPaused(activity: Activity) {
        Timber.d( "onActivityPaused")
    }

    override fun onActivityResumed(activity: Activity) {
        Timber.d("onActivityResumed")
    }

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle?
    ) {
        Timber.d("onActivitySaveInstanceState")
    }

    override fun onActivityStarted(activity: Activity) {
        Timber.d("onActivityStarted")
    }

    override fun onActivityStopped(activity: Activity) {
        Timber.d("onActivityStopped")
    }
}