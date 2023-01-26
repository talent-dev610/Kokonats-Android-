package com.biiiiit.kokonats.ui.game.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.TnmtPayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @Author yo_hack
 * @Date 2021.11.03
 * @Description tnmt payout adapter
 **/
class TnmtPlayPayoutAdapter :
    BaseQuickAdapter<TnmtPayout, BaseViewHolder>(R.layout.item_tnmt_payout_rule) {

    init {
        setDiffCallback(object : DiffUtil.ItemCallback<TnmtPayout>() {
            override fun areItemsTheSame(oldItem: TnmtPayout, newItem: TnmtPayout): Boolean = true

            override fun areContentsTheSame(oldItem: TnmtPayout, newItem: TnmtPayout): Boolean =
                oldItem.startRank == newItem.startRank &&
                        oldItem.endRank == newItem.endRank &&
                        oldItem.payout == newItem.payout

        })
    }

    override fun convert(holder: BaseViewHolder, item: TnmtPayout) {
        holder.setText(R.id.tv_reward, "+${item.payout}")
            .setText(
                R.id.tv_rank, if (item.startRank == item.endRank) {
                    // 单独一个
                    context.getString(R.string.payout_one, item.startRank)
                } else {
                    context.getString(R.string.payout_multi, item.startRank, item.endRank)
                }
            )

    }
}