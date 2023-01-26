package com.biiiiit.kokonats.ui.game.ui.adapter

import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.TournamentPlay
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author yo_hack
 * @Date 2021.11.04
 * @Description 用户 play history adapter
 **/
class UserPlayHistoryAdapter :
    BaseQuickAdapter<TournamentPlay, BaseViewHolder>(R.layout.item_tnmt_play_history) {

    private var timeFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())

    private val wait: String by lazy {
        context.getString(R.string.waiting)
    }

    override fun convert(holder: BaseViewHolder, item: TournamentPlay) {
        var timeAndName = timeFormat.format(item.timestamp)

        val name = if (item.type == 0) {
            item.tournamentName
        } else {
            if (item.userName.isNullOrBlank()) {
                wait
            } else {
                timeAndName += " vs " + item.userName
            }

            item.matchName
        }


        holder.setText(R.id.tv_name, name)
            .setText(R.id.tv_energy, "-${item.energy}")
            .setText(R.id.tv_time, timeAndName)
            .setGone(R.id.ll_reward, item.kokoAmount <= 0)
            .setText(R.id.tv_reward, "+ ${item.kokoAmount}")


    }
}