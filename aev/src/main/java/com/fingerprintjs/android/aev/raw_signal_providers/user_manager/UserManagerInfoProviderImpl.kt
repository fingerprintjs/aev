package com.fingerprintjs.android.aev.raw_signal_providers.user_manager

import android.os.Build
import android.os.UserManager

internal class UserManagerInfoProviderImpl(
    private val userManager: UserManager
) : UserManagerInfoProvider {

    override fun getUserProfileInfo(): UserProfileInfo = runCatching {
        val profilesCount = userManager.userProfiles.size
        val isManagedProfile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            userManager.isManagedProfile
        } else null
        val isSystemUser = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            userManager.isSystemUser
        } else null

        UserProfileInfo(
            userProfilesCount = profilesCount,
            isManagedProfile = isManagedProfile,
            isSystemUser = isSystemUser,
        )
    }.getOrDefault(
        UserProfileInfo(
            userProfilesCount = null,
            isManagedProfile = null,
            isSystemUser = null,
        )
    )
}