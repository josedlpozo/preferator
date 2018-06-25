package com.sloydev.preferator.presenter

import com.sloydev.preferator.data.PreferatorDataSource
import com.sloydev.preferator.model.PreferatorConfig
import com.sloydev.preferator.model.Preferences

class PreferatorPresenter(private val view: View, private val config: PreferatorConfig, private val dataSource: PreferatorDataSource) {

    interface View {
        fun render(preferences: Preferences)
    }

    fun start() = dataSource.get(config).also(view::render)
}