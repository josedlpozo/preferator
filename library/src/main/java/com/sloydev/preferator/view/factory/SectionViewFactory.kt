package com.sloydev.preferator.view.factory

import android.content.Context
import android.support.v7.widget.PopupMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sloydev.preferator.R
import com.sloydev.preferator.SdkFilter
import com.sloydev.preferator.model.Preference
import com.sloydev.preferator.model.Type

class SectionViewFactory {

    private val factory: EditorViewFactory by lazy { EditorViewFactory() }

    fun createSection(context: Context, parent: ViewGroup, preference: Preference): View {
        val sectionView = LayoutInflater.from(context).inflate(R.layout.item_section, parent, false)
        val sectionNameContainer = sectionView.findViewById(R.id.section_name_container) as LinearLayout
        val sectionNameView = sectionView.findViewById(R.id.section_name) as TextView
        val sectionArrowView = sectionView.findViewById(R.id.section_arrow) as ImageView
        val itemsView = sectionView.findViewById(R.id.section_items) as ViewGroup

        sectionNameView.text = preference.name
        sectionNameContainer.setOnClickListener {
            if (itemsView.visibility == View.VISIBLE) {
                itemsView.visibility = View.GONE
                sectionArrowView.setImageResource(R.drawable.ic_arrow_expand_black_24dp)
            } else {
                itemsView.visibility = View.VISIBLE
                sectionArrowView.setImageResource(R.drawable.ic_arrow_collapse_black_24dp)
            }
        }

        // Auto-collapse sdks
        if (SdkFilter.isSdkPreference(preference.name)) {
            itemsView.visibility = View.GONE
            sectionArrowView.setImageResource(R.drawable.ic_arrow_expand_black_24dp)
        }

        preference.items.map {
            val prefKey = it.first
            val prefValue = it.second
            val prefType = Type.of(prefValue)

            val itemView = LayoutInflater.from(context).inflate(R.layout.item_preference, itemsView, false)
            val nameView = itemView.findViewById(R.id.pref_name) as TextView
            val typeView = itemView.findViewById(R.id.pref_type) as TextView
            val moreView = itemView.findViewById(R.id.pref_more) as ImageView

            nameView.text = prefKey
            typeView.text = prefType.typeName

            val editorContainer = itemView.findViewById(R.id.pref_value_editor_container) as ViewGroup
            val editorView = factory.createView(context, preference.sharedPreferences, prefKey, prefValue, prefType)
            editorContainer.addView(editorView)


            val moreOptionsMenu = PopupMenu(context, moreView)
            moreOptionsMenu.inflate(R.menu.pref_more_options)
            moreOptionsMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                if (item.itemId == R.id.menu_pref_delete) {
                    preference.sharedPreferences.edit().remove(prefKey).apply()
                    itemsView.removeView(itemView)
                    return@OnMenuItemClickListener true
                } else if (item.itemId == R.id.menu_pref_share) {
                    /*val sharedMessage = String.format("\"%s\":\"%s\"", prefKey, prefValue.toString())
                    ShareCompat.IntentBuilder.from(context)
                        .setText(sharedMessage)
                        .setType("text/plain")
                        .startChooser()*/
                    return@OnMenuItemClickListener true
                }
                false
            })
            moreView.setOnClickListener { moreOptionsMenu.show() }


            itemsView.addView(itemView)
        }

        return sectionView
    }
}