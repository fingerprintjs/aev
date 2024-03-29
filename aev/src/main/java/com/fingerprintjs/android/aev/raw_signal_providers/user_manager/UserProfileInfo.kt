package com.fingerprintjs.android.aev.raw_signal_providers.user_manager

internal class UserProfileInfo(
    /**
     * Count of profiles associated with the current user
     */
    val userProfilesCount: Int?,
    /**
     * Tells whether our app is executed inside managed, or work, profile
     */
    val isManagedProfile: Boolean?,
    /**
     * Tells whether current user is system user
     */
    val isSystemUser: Boolean?,
)