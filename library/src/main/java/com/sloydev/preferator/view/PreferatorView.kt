package com.sloydev.preferator.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.sloydev.preferator.data.PreferatorDataSource
import com.sloydev.preferator.model.PreferatorConfig
import com.sloydev.preferator.model.Preferences
import com.sloydev.preferator.presenter.PreferatorPresenter
import com.sloydev.preferator.view.factory.SectionViewFactory

class PreferatorView @JvmOverloads constructor(context: Context, val attr: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attr, defStyleAttr), PreferatorPresenter.View {

    var config: PreferatorConfig = PreferatorConfig()
        set(value) {
            presenter.config = value
            field = value
        }

    private val sectionViewFactory: SectionViewFactory by lazy { SectionViewFactory() }
    private val presenter: PreferatorPresenter by lazy { PreferatorPresenter(this, PreferatorDataSource(context)) }

    init {
        orientation = VERTICAL
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