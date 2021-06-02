package com.base.medicalgate.data.managers

import com.mahmoudsalah.swansontask.MainApplication
import com.squareup.moshi.Moshi

/**
 * A class used to handle user's sensitive data
 */
class AuthManager {
    private val prefsManager by lazy { SharedPreferencesManager(MainApplication.appContext) }
}