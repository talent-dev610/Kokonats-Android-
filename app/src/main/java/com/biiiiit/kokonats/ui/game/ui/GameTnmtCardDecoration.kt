package com.biiiiit.kokonats.ui.game.ui

import android.graphics.Rect
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.biiiiit.kokonats.R
import com.biiiiit.lib_base.utils.dp2px
import com.chad.library.adapter.base.BaseQuickAdapter
import java.lang.ref.WeakReference

/**
 * @Author yo_hack
 * @Date 2021.11.01
 * @Description
 **/
class GameTnmtCardDecoration(val type: Int) : RecyclerView.ItemDecoration() {

    companion object {
        const val TYPE_TAB_TNMT = 1
        const val TYPE_TAB_GAME = 2
        const val TYPE_GAME_TNMT = 3
    }

    private var left = 0
    private var right = 0
    private var dpGame = 0


    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        if (left <= 0) {
            val context = parent.context
            left = context.resources.getDimensionPixelSize(R.dimen.common_margin_53) +
                    3.dp2px(context)
            right = 47.dp2px(context)
            dpGame = if (type == 1) {
                10.dp2px(context)
            } else {
                (6.5f.dp2px(context)).toInt()
            }
        }

        val size = (parent.adapter as BaseQuickAdapter<*, *>).data.size
        when (parent.getChildAdapterPosition(view)) {
            0 -> {
                outRect.left = left
                outRect.right = dpGame
            }
            size - 1 -> {
                outRect.left = dpGame
                outRect.right = right
            }
            else -> {
                outRect.left = dpGame
                outRect.right = dpGame
            }

        }

    }
}