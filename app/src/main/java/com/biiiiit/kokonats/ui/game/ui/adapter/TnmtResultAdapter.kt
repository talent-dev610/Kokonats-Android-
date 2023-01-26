package com.biiiiit.kokonats.ui.game.ui.adapter

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.TournamentPlay
import com.biiiiit.lib_base.data.LoginUser
import com.biiiiit.lib_base.utils.SP_LOGIN_USER
import com.biiiiit.lib_base.utils.getAny
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @Author yo_hack
 * @Date 2021.12.08
 * @Description tnmt result adapter
 **/
class TnmtResultAdapter :
    BaseQuickAdapter<TournamentPlay, BaseViewHolder>(R.layout.item_tnmt_result_score) {

    private val tvColor = R.color.color_white
    private val bgColor = R.color.blue_1a4870ff

    private val tvMeColor = R.color.yellow_ffcb00
    private val bgMeColor = R.color.black_19ffcb00

    private var user = getAny<LoginUser>(SP_LOGIN_USER)

    override fun convert(holder: BaseViewHolder, item: TournamentPlay) {
        val t: Int
        val b = if (user?.userName == item.userName) {
            t = tvMeColor
            bgMeColor
        } else {
            t = tvColor
            bgColor
        }

        val gs = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(ContextCompat.getColor(context, b))
        }


        val pos = holder.layoutPosition + 1

        holder.setText(R.id.tv_score, item.score.toString())
            .setText(R.id.tv_name, item.userName)
            .setText(R.id.tv_rank, pos.toString())
            .setTextColorRes(R.id.tv_score, t)
            .setTextColorRes(R.id.tv_name, t)
            .setTextColorRes(R.id.tv_rank, tvColor)

        holder.getViewOrNull<View>(R.id.tv_rank)?.background = gs
    }
}