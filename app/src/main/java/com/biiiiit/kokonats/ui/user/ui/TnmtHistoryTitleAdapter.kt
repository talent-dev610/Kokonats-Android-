package com.biiiiit.kokonats.ui.user.ui

import com.biiiiit.kokonats.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @Author yo_hack
 * @Date 2022.02.24
 * @Description
 **/
class TnmtHistoryTitleAdapter :
    BaseQuickAdapter<Pair<Long, String>, BaseViewHolder>(R.layout.item_history_game_title) {
    override fun convert(holder: BaseViewHolder, item: Pair<Long, String>) {
        holder.setText(R.id.tv_title, item.second)
    }
}