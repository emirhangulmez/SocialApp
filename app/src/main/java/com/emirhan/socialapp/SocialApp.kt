package com.emirhan.socialapp

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import dagger.hilt.android.HiltAndroidApp
import java.lang.ref.WeakReference

@HiltAndroidApp
class SocialApp : Application(), Application.ActivityLifecycleCallbacks {

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        // had to be declared
    }

    override fun onActivityStarted(activity: Activity) {
        // had to be declared
    }

    override fun onActivityStopped(activity: Activity) {
        // had to be declared
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
        // had to be declared
    }

    override fun onActivityDestroyed(activity: Activity) {
        // had to be declared
    }

    override fun onActivityResumed(activity: Activity) {
        activityRef = WeakReference(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        // had to be declared
    }

    companion object {
        private var activityRef: WeakReference<Context>? = null
        val currentActivityContext: Context?
            get() = activityRef?.get()
    }
}