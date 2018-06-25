package com.sloydev.preferator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.sloydev.preferator.model.PreferatorConfig
import com.sloydev.preferator.view.PreferatorView

class PreferatorActivity : AppCompatActivity() {

    companion object {
        private const val CONFIG_KEY = "preferator_config"
        fun intent(context: Context, config: PreferatorConfig) : Intent = Intent(context, PreferatorActivity::class.java).apply {
            putExtra(CONFIG_KEY, config)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prefereitor)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.subtitle = getApplicationName()
        }

        val config = intent.extras.getSerializable(CONFIG_KEY) as PreferatorConfig
        val view = findViewById<PreferatorView>(R.id.preferatorView)
        view.config = config
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
