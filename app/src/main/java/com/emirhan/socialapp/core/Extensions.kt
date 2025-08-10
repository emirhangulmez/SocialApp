package com.emirhan.socialapp.core

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.android.gms.common.GoogleApiAvailability

object Extensions {

    fun Context.checkCompatibleWithCredentialManager(): Boolean {
        return try {
            // Attempt to create a CredentialManager instance to see if it is supported
            CredentialManager.create(this)
            // Check if Google Play Services is available
            val googleApiAvailability = GoogleApiAvailability.getInstance()
            val status = googleApiAvailability.isGooglePlayServicesAvailable(this)
            return status == com.google.android.gms.common.ConnectionResult.SUCCESS
        } catch (_: Exception) {
            // If an exception occurs, CredentialManager is not supported
            false
        }
    }
}