package com.sloydev.preferator.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.sloydev.preferator.data.PreferatorDataSource
import com.sloydev.preferator.data.Preferences
import com.sloydev.preferator.presenter.PreferatorPresenter
import com.sloydev.preferator.view.factory.SectionViewFactory

class PreferatorView @JvmOverloads constructor(context: Context, val attr: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attr, defStyleAttr), PreferatorPresenter.View {

    private val sectionViewFactory: SectionViewFactory by lazy { SectionViewFactory() }
    private val presenter: PreferatorPresenter = PreferatorPresenter(this, PreferatorDataSource(context))

    init {
        presenter.start()
    }

    override fun render(preferences: Preferences) {
        removeAllViews()
        preferences.items.map {
            val view = sectionViewFactory.createSection(context, this, it)
            addView(view)
        }
    }
}