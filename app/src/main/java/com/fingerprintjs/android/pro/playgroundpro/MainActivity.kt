package com.fingerprintjs.android.pro.playgroundpro


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tab_layout)
        tabs.setupWithViewPager(viewPager)
    }
}