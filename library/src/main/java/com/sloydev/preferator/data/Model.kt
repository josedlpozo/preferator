package com.sloydev.preferator.data

import android.content.SharedPreferences

typealias PreferenceItem = Pair<String, Any>

data class Preference(val name: String, val items: List<PreferenceItem> = listOf(), val sharedPreferences: SharedPreferences)

data class Preferences(val items: List<Preference> = listOf())