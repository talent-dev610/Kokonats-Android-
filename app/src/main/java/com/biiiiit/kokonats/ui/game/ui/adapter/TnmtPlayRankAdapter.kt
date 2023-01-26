package com.biiiiit.kokonats.ui.game.ui.adapter

import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.TournamentPlay
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @Author yo_hack
 * @Date 2021.11.04
 * @Description tournament 排名
 **/
class TnmtPlayRankAdapter :
    BaseQuickAdapter<TournamentPlay, BaseViewHolder>(R.layout.item_tnmt_play_rank) {


    override fun convert(holder: BaseViewHolder, item: TournamentPlay) {
        holder.setText(R.id.tv_score, item.score.toString())
            .setText(R.id.tv_user, item.userName)

        val pos = holder.layoutPosition
        val color = when (pos) {
            0 -> R.color.red_ef5da8
            1 -> R.color.red_f178b6
            2 -> R.color.red_fcddec
            else -> R.color.blue_1a4870ff
        }
        val gs = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(ContextCompat.getColor(context, color))
        }
        holder.setText(R.id.tv_rank, (pos + 1).toString())
        holder.getView<TextView>(R.id.tv_rank).background = gs
    }
}