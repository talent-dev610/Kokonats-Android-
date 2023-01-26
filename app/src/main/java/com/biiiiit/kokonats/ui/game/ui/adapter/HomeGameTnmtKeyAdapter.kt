package com.biiiiit.kokonats.ui.game.ui.adapter

import com.biiiiit.kokonats.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @Author yo_hack
 * @Date 2021.10.31
 * @Description 首页 tab 中的 game --> game card --> person
 **/
class HomeGameTnmtKeyAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_home_game_tab_tnmt_key) {


    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv_title, item)

        val txtColor: Int
        val bgRes: Int

        val pos = holder.layoutPosition
        if (pos % 2 == 1) {
            txtColor = R.color.green_00cb75
            bgRes = R.drawable.rect11_green2b3a34
        } else {
            bgRes = R.drawable.rect11_black262426
            txtColor = if (pos % 4 == 0) {
                R.color.yellow_ffcb00
            } else {
                R.color.red_dc5328
            }
        }


        holder.setText(R.id.tv_title, item)
            .setBackgroundResource(R.id.tv_title, bgRes)
            .setTextColorRes(R.id.tv_title, txtColor)
    }
}