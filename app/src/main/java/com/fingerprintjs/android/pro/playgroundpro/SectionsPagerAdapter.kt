package com.fingerprintjs.android.pro.playgroundpro

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.fingerprintjs.android.pro.playgroundpro.settings.SettingsFragment
import com.fingerprintjs.android.pro.playgroundpro.signals_screen.ReceiveTokenFragment
import com.fingerprintjs.android.pro.playgroundpro.verification_screen.VerifyTokenFragment


class SectionsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val PAGE_TITLES = listOf(
        "Receive token",
        "Verify token",
        "Settings"
    )


    override fun getCount(): Int  = 3

    override fun getItem(i: Int): Fragment {
        return when(i) {
            0 -> ReceiveTokenFragment()
            1 -> VerifyTokenFragment()
            2 -> SettingsFragment()
            else -> ReceiveTokenFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return PAGE_TITLES[position]
    }
}