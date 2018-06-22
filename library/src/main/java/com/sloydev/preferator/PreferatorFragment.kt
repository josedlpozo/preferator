package com.sloydev.preferator

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.ShareCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PopupMenu
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sloydev.preferator.model.Type
import com.sloydev.preferator.view.factory.EditorViewFactory
import java.io.File
import java.util.*

class PreferatorFragment : Fragment() {

    private var sectionsView: ViewGroup? = null

    private val factory: EditorViewFactory by lazy { EditorViewFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_preferator, container, false)
        sectionsView = view.findViewById(R.id.sections)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        parsePreferences()
    }

    private fun parsePreferences() {
        if (activity == null) return
        val rootPath = (activity as AppCompatActivity).applicationInfo.dataDir + "/shared_prefs"
        val prefsFolder = File(rootPath)
        prefsFolder.list()
                .map {
                    truncateXmlExtension(it)
                }
                .sortedWith(compareBy({ SdkFilter.isSdkPreference(it) }, { it }))
                .forEach {
                    generateForm(it)
                }
    }

    private fun truncateXmlExtension(it: String): String = if (it.endsWith(".xml"))
        it.substring(0, it.indexOf(".xml"))
    else
        it

    private fun generateForm(prefsName: String) {
        val preferences = getSharedPreferences(prefsName)
        val entries = ArrayList<Pair<String, Any>>()
        for ((key, value) in preferences.all) {
            Log.d(PreferatorActivity.TAG, String.format("(%s) %s = %s", prefsName, key, value.toString()))
            entries.add(Pair.create<String, Any>(key, value))
        }

        addSection(prefsName, entries, preferences)
    }

    private fun addSection(sectionTitle: String, entries: List<Pair<String, Any>>, preferences: SharedPreferences) {
        val sectionView = LayoutInflater.from(context).inflate(R.layout.item_section, sectionsView, false)
        val sectionNameContainer = sectionView.findViewById(R.id.section_name_container) as LinearLayout
        val sectionNameView = sectionView.findViewById(R.id.section_name) as TextView
        val sectionArrowView = sectionView.findViewById(R.id.section_arrow) as ImageView
        val itemsView = sectionView.findViewById(R.id.section_items) as ViewGroup

        sectionNameView.text = sectionTitle
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
        if (SdkFilter.isSdkPreference(sectionTitle)) {
            itemsView.visibility = View.GONE
            sectionArrowView.setImageResource(R.drawable.ic_arrow_expand_black_24dp)
        }

        context?.let {
            for (pref in entries) {
                val prefKey = pref.first
                val prefValue = pref.second
                if (prefKey == null || prefValue == null) continue
                val prefType = Type.of(prefValue)

                val itemView = LayoutInflater.from(context).inflate(R.layout.item_preference, itemsView, false)
                val nameView = itemView.findViewById(R.id.pref_name) as TextView
                val typeView = itemView.findViewById(R.id.pref_type) as TextView
                val moreView = itemView.findViewById(R.id.pref_more) as ImageView

                nameView.text = prefKey
                typeView.text = prefType.typeName

                val editorContainer = itemView.findViewById(R.id.pref_value_editor_container) as ViewGroup
                val editorView = factory.createView(it, preferences, prefKey, prefValue, prefType)
                editorContainer.addView(editorView)


                val moreOptionsMenu = PopupMenu(it, moreView)
                moreOptionsMenu.inflate(R.menu.pref_more_options)
                moreOptionsMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    if (item.itemId == R.id.menu_pref_delete) {
                        preferences.edit().remove(prefKey).apply()
                        itemsView.removeView(itemView)
                        return@OnMenuItemClickListener true
                    } else if (item.itemId == R.id.menu_pref_share) {
                        val sharedMessage = String.format("\"%s\":\"%s\"", prefKey, prefValue.toString())
                        ShareCompat.IntentBuilder.from(activity)
                                .setText(sharedMessage)
                                .setType("text/plain")
                                .startChooser()
                        return@OnMenuItemClickListener true
                    }
                    false
                })
                moreView.setOnClickListener { moreOptionsMenu.show() }


                itemsView.addView(itemView)
            }
        }
        sectionsView?.addView(sectionView)
    }

    private fun getSharedPreferences(name: String): SharedPreferences {
        if (activity == null) throw IllegalAccessException()
        return (activity as AppCompatActivity).getSharedPreferences(name, Context.MODE_MULTI_PROCESS)
    }

    companion object {
        fun newInstance(): PreferatorFragment = PreferatorFragment()
    }
}
