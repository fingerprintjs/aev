package com.fingerprintjs.android.aev.demo


import android.content.Context
import android.os.Build
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey


interface ApplicationPreferences {
    fun getEndpointUrl(): String
    fun getPublicApKey(): String
    fun getPrivateApiKey(): String

    fun setEndpointUrl(endpointUrl: String)
    fun setPublicApiKey(apiToken: String)
    fun setPrivateApiKey(apiToken: String)

    fun setAgreementToTheTerms(agreed: Boolean)
    fun getAgreementToTheTerms(): Boolean
}

class ApplicationPreferencesImpl(context: Context) : ApplicationPreferences {

    private val preferences = createPreferences(context)

    private val defaultEndpointUrl = context.getString(R.string.defaultEndpointUrl)
    private val defaultPublicApiToken = context.getString(R.string.defaultPublicApiToken)
    private val defaultPrivateApiToken = context.getString(R.string.defaultPrivateApiToken)

    private val API_KEY = context.getString(R.string.apiTokenKey)
    private val PRIVATE_API_KEY = context.getString(R.string.privateApiTokenKey)
    private val ENDPOINT_URL_KEY = context.getString(R.string.endpointUrlKey)
    private val AGREEMENT_TO_THE_TERMS_KEY = context.getString(R.string.agreementToTheTermsKey)

    override fun getEndpointUrl() =
        preferences.getString(ENDPOINT_URL_KEY, null) ?: defaultEndpointUrl

    override fun getPublicApKey() = preferences.getString(API_KEY, null) ?: defaultPublicApiToken

    override fun getPrivateApiKey(): String = preferences.getString(PRIVATE_API_KEY, null) ?: defaultPrivateApiToken

    override fun setEndpointUrl(endpointUrl: String) {
        preferences.edit().putString(ENDPOINT_URL_KEY, endpointUrl).apply()
    }

    override fun setPublicApiKey(apiToken: String) {
        preferences.edit().putString(API_KEY, apiToken).apply()
    }

    override fun setPrivateApiKey(apiToken: String) {
        preferences.edit().putString(PRIVATE_API_KEY, apiToken).apply()
    }

    override fun setAgreementToTheTerms(agreed: Boolean) {
        preferences.edit().putBoolean(AGREEMENT_TO_THE_TERMS_KEY, agreed).apply()
    }

    override fun getAgreementToTheTerms(): Boolean {
        return preferences.getBoolean(AGREEMENT_TO_THE_TERMS_KEY, false)
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