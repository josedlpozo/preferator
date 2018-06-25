package com.sloydev.preferator

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

class PreferatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prefereitor)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.subtitle = getApplicationName()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when {
        item.itemId == android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun getApplicationName(): String {
        val applicationInfo = applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else getString(stringId)
    }
}
