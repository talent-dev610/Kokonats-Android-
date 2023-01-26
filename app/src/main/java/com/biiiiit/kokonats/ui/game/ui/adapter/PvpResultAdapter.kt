package com.biiiiit.kokonats.ui.game.ui.adapter

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.PvpUserScore
import com.biiiiit.lib_base.data.LoginUser
import com.biiiiit.lib_base.utils.SP_LOGIN_USER
import com.biiiiit.lib_base.utils.getAny
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.auth.User

/**
 * @Author yo_hack
 * @Date 2022.01.13
 * @Description pvp result adapter
 **/
class PvpResultAdapter : BaseQuickAdapter<PvpUserScore, BaseViewHolder>(R.layout.item_tnmt_result_score) {


    private val tvColor = R.color.color_white

    private val wColor = R.color.yellow_fabe2c
    private val lColor = R.color.gray_d8d8d8

    private var user = getAny<LoginUser>(SP_LOGIN_USER)
    var myStatus: String = ""

    override fun convert(holder: BaseViewHolder, item: PvpUserScore) {
        val isMe = item.username == user?.userName

        val gs = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(ContextCompat.getColor(context, R.color.blue_1a4870ff))
        }

        val color = if (isMe) {
            wColor
        } else {
            lColor
        }

        val str: String = if (myStatus == "W" || myStatus == "L") {
            if ((isMe && myStatus == "W") || (!isMe && myStatus == "L")) {
                "W"
            } else {
                "L"
            }
        } else if (myStatus == "D") {
            "D"
        } else {
            // 结算中 ing
            (1 + holder.layoutPosition).toString()
        }



        holder.setText(R.id.tv_score, item.totalScore)
            .setText(R.id.tv_name, item.username)
            .setTextColorRes(R.id.tv_score, tvColor)
            .setTextColorRes(R.id.tv_name, tvColor)
            .setText(R.id.tv_rank, str)
            .setTextColorRes(R.id.tv_rank, color)

        holder.getViewOrNull<View>(R.id.tv_rank)?.background = gs
    }
}