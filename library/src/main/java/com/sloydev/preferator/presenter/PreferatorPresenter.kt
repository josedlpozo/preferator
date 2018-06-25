package com.sloydev.preferator.presenter

import com.sloydev.preferator.data.PreferatorDataSource
import com.sloydev.preferator.data.Preferences

class PreferatorPresenter(private val view: View, private val dataSource: PreferatorDataSource) {

    interface View {
        fun render(preferences: Preferences)
    }

    fun start() = dataSource.get().also(view::render)
}