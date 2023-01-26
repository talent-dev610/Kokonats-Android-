package com.biiiiit.kokonats.ui.main.tutorial

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.biiiiit.kokonats.R
import com.biiiiit.lib_base.utils.logi

class TutorialAdapter(private val onGetStarted: OnGetStarted) : PagerAdapter() {


    override fun getCount(): Int {
        return 3
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = when (position) {
            0 -> LayoutInflater.from(container.context).inflate(R.layout.tutorial_step_1, container, false)
            1 -> LayoutInflater.from(container.context).inflate(R.layout.tutorial_step_2, container, false)
            2 -> LayoutInflater.from(container.context).inflate(R.layout.tutorial_step_3, container, false)
            else -> LayoutInflater.from(container.context).inflate(R.layout.tutorial_step_1, container, false)
        }
        if (position == 2) {
            val tapToStart = view.findViewById<TextView>(R.id.tv_click)
            tapToStart.setOnClickListener(View.OnClickListener {
                onGetStarted.start()
            })
        }
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}