package com.fingerprintjs.android.aev.signals.user_profile

import android.content.Context
import android.os.Build
import android.os.UserManager

internal class UserProfileSignalProviderImpl(
    private val context: Context
) : UserProfileSignalProvider {

    override fun getUserProfileSignal() = UserProfileSignal(
        runCatching {
            val userManager = context.getSystemService(Context.USER_SERVICE) as UserManager

            val profilesCount = userManager.userProfiles.size
            val isManagedProfile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                userManager.isManagedProfile
            } else null
            val isSystemUser = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                userManager.isSystemUser
            } else null

            UserProfileData(
                userProfilesCount = profilesCount,
                isManagedProfile = isManagedProfile,
                isSystemUser = isSystemUser,
            )
        }.getOrDefault(
            UserProfileData(
                userProfilesCount = null,
                isManagedProfile = null,
                isSystemUser = null,
            )
        )
    )
}