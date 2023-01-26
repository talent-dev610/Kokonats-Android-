package com.biiiiit.kokonats.ui.game.ui.adapter

import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.TnmtPlayer
import com.biiiiit.kokonats.utils.loadCircleAvatar
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @Author yo_hack
 * @Date 2021.10.31
 * @Description 首页 tab 中的 game --> game card --> person
 **/
class HomeGameTnmtPersonAdapter :
    BaseMultiItemQuickAdapter<MultiTnmtPersonEntity, BaseViewHolder>() {

    init {
        addItemType(MultiTnmtPersonEntity.TYPE_PLAYER, R.layout.item_home_game_tab_tnmt_person)
        addItemType(MultiTnmtPersonEntity.TYPE_NUMBER, R.layout.item_home_game_tab_tnmt_person_count)
    }


    override fun convert(holder: BaseViewHolder, item: MultiTnmtPersonEntity) {
        when (item.itemType) {
            MultiTnmtPersonEntity.TYPE_PLAYER -> {
                loadCircleAvatar(holder.getView(R.id.iv_avatar), item.player?.picture.toString(), context)
            }
            else -> {
                item.pair?.let {
                    holder.setText(R.id.tv_desc, "${it.first}/${it.second}")
                }
            }

        }

    }
}


class MultiTnmtPersonEntity(override val itemType: Int) : MultiItemEntity {

    var pair: Pair<Int, Int>? = null

    var player: TnmtPlayer? = null

    constructor(p: Pair<Int, Int>) : this(TYPE_NUMBER) {
        this.pair = p
    }

    constructor(p: TnmtPlayer) : this(TYPE_PLAYER) {
        this.player = p
    }


    companion object {
        val TYPE_PLAYER = 1
        val TYPE_NUMBER = 2
    }

}