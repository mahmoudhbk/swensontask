package com.base.medicalgate.data.managers

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.method.TextKeyListener.clear
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class SharedPreferencesManager(context: Context) {

    private val securedSharedPrefs by lazy {
        EncryptedSharedPreferences.create(
            "medical_gate_secured_shared_preferences",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val sPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(
            context
        )
    }

    fun putBoolean(tag: String, value: Boolean, secure: Boolean = false) {
        sPreferences.edit(false) { putBoolean(tag, value) }
    }

    fun getBoolean(tag: String, defValue: Boolean): Boolean {
        return sPreferences.getBoolean(tag, defValue)

    }

    fun putString(tag: String, str: String, secure: Boolean = false) {
        if (secure) {
            securedSharedPrefs.edit(false) { putString(tag, str) }
        } else {
            sPreferences.edit(false) { putString(tag, str) }
        }
    }

    fun getString(tag: String, defStr: String?, secure: Boolean = false): String? {
        return if (secure) {
            securedSharedPrefs.getString(tag, defStr)
        } else {
            sPreferences.getString(tag, defStr)
        }

    }

    fun putInt(tag: String, defValue: Int, secure: Boolean = false) {
        if (secure) {
            securedSharedPrefs.edit(false) { putInt(tag, defValue) }
        } else {
            sPreferences.edit(false) { putInt(tag, defValue) }
        }
    }

    fun getInt(tag: String, defValue: Int, secure: Boolean = false): Int {
        return if (secure) {
            securedSharedPrefs.getInt(tag, defValue)
        } else {
            sPreferences.getInt(tag, defValue)
        }
    }

    fun remove(tag: String, secure: Boolean = false) {
        if (secure) {
            securedSharedPrefs.edit(false) { remove(tag) }
        } else {
            sPreferences.edit(false) { remove(tag) }
        }
    }

    fun clearPreferences() {
        securedSharedPrefs.edit(false) { clear() }
        sPreferences.edit(false) { clear() }
    }

}