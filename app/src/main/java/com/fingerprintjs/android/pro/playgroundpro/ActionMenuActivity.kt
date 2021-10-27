package com.fingerprintjs.android.pro.playgroundpro


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.fingerprintjs.android.pro.playgroundpro.demo.GITHUB_REPO_URL
import com.fingerprintjs.android.pro.playgroundpro.dialogs.SettingsDialog


open class ActionMenuActivity : AppCompatActivity() {
    protected lateinit var applicationPreferences: ApplicationPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applicationPreferences = ApplicationPreferencesImpl(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> openSettings()
            R.id.menu_repository -> openLink(Uri.parse(GITHUB_REPO_URL))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openSettings() {
        SettingsDialog(this, applicationPreferences).showSettings()
    }

    protected fun openLink(webpage: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}