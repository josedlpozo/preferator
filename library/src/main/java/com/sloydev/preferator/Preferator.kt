package com.sloydev.preferator

import android.content.Context
import com.sloydev.preferator.model.PreferatorConfig

object Preferator {

    fun launch(context: Context, config: PreferatorConfig = PreferatorConfig()) {
        context.startActivity(PreferatorActivity.intent(context, config))
    }
}