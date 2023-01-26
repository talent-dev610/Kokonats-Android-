package com.biiiiit.kokonats.ui.game.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.biiiiit.kokonats.R
import com.biiiiit.lib_base.BaseApp
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * @Author yo_hack
 * @Date 2021.11.21
 * @Description game tnmt tab title decoration
 **/
class GameTnmtTabTitleDecoration : RecyclerView.ItemDecoration() {

    private val start = BaseApp.app.resources.getDimensionPixelSize(R.dimen.common_margin_53)
    private val end = start - 2 * BaseApp.app.resources.getDimensionPixelSize(R.dimen.w14)

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {

        val size = (parent.adapter as BaseQuickAdapter<*, *>).data.size

        if (size > 0) {
            when (parent.getChildAdapterPosition(view)) {
                0 -> {
                    outRect.left = start
                }
                size - 1 -> {
                    outRect.right = end
                }
            }
        }
    }
}