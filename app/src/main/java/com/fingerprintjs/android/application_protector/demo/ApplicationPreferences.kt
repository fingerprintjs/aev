package com.fingerprintjs.android.application_protector.demo


import android.content.Context
import android.os.Build
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey


interface ApplicationPreferences {
    fun getEndpointUrl(): String
    fun getApiToken(): String

    fun setEndpointUrl(endpointUrl: String)
    fun setApiToken(apiToken: String)
}

class ApplicationPreferencesImpl(context: Context) : ApplicationPreferences {

    private val preferences = createPreferences(context)

    private val defaultEndpointUrl = context.getString(R.string.defaultEndpointUrl)
    private val defaultAPIToken = context.getString(R.string.defaultApiToken)

    private val API_TOKEN_KEY = context.getString(R.string.apiTokenKey)
    private val ENDPOINT_URL_KEY = context.getString(R.string.endpointUrlKey)

    override fun getEndpointUrl() =
        preferences.getString(ENDPOINT_URL_KEY, null) ?: defaultEndpointUrl

    override fun getApiToken() = preferences.getString(API_TOKEN_KEY, null) ?: defaultAPIToken

    override fun setEndpointUrl(endpointUrl: String) {
        preferences.edit().putString(ENDPOINT_URL_KEY, endpointUrl).apply()
    }

    override fun setApiToken(apiToken: String) {
        preferences.edit().putString(API_TOKEN_KEY, apiToken).apply()
    }

    private fun createPreferences(context: Context) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            EncryptedSharedPreferences.create(
                context,
                PREFERENCES_FILENAME,
                MasterKey(context),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } else {
            PreferenceManager.getDefaultSharedPreferences(context)
        }
}

private const val PREFERENCES_FILENAME = "fpjs_prefs"