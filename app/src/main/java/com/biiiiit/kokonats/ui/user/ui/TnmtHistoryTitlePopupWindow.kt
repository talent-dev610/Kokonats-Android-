package com.biiiiit.kokonats.ui.user.ui

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.biiiiit.kokonats.databinding.PopHistoryGameTitleBinding
import com.biiiiit.lib_base.utils.dp2px
import com.biiiiit.lib_base.utils.getScreenWidth

/**
 * @Author yo_hack
 * @Date 2022.02.24
 * @Description
 **/
class TnmtHistoryTitlePopupWindow
constructor(val binding: PopHistoryGameTitleBinding, data: MutableList<Pair<Long, String>>, onClick: (pos: Int) -> Unit)
    : PopupWindow(binding.root) {





    init {
        val ctx = binding.root.context

        width = (getScreenWidth(ctx) * 0.5).toInt()
        height = width

        val adapter = TnmtHistoryTitleAdapter()
        binding.rcvTitle.layoutManager = LinearLayoutManager(ctx)
        binding.rcvTitle.adapter = adapter
        adapter.setOnItemClickListener { _, _, pos ->
            onClick.invoke(pos)
            dismiss()
        }
        adapter.setNewInstance(data)
        setOnDismissListener {
            binding.rcvTitle.adapter = null
        }
        val ga = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dp2px(ctx, 6f)
            setColor(Color.WHITE)
        }
        setBackgroundDrawable(ga)
        isOutsideTouchable = true
        isFocusable = true
    }


}