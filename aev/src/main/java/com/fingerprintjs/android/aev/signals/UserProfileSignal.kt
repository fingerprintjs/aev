package com.fingerprintjs.android.aev.signals

import com.fingerprintjs.android.aev.raw_signal_providers.user_manager.UserProfileInfo
import com.fingerprintjs.android.fingerprint.signal_providers.Signal

internal class UserProfileSignal(value: UserProfileInfo) :
    Signal<UserProfileInfo>(USER_PROFILE_SIGNAL_NAME, value) {

    override fun toMap() = mapOf(
        VALUE_KEY to listOfNotNull(
            value.userProfilesCount?.let { USER_PROFILES_COUNT_KEY to it },
            value.isManagedProfile?.let { IS_MANAGED_PROFILE_KEY to it },
            value.isSystemUser?.let { IS_SYSTEM_USER_KEY to it },
        ).toMap()
    )
}

private const val USER_PROFILE_SIGNAL_NAME = "userProfileInfo"
private const val USER_PROFILES_COUNT_KEY = "userProfiles"
private const val IS_MANAGED_PROFILE_KEY = "isManaged"
private const val IS_SYSTEM_USER_KEY = "isSystemUser"