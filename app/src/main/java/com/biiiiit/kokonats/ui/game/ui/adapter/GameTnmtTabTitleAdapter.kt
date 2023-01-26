package com.biiiiit.kokonats.ui.game.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.biiiiit.kokonats.R
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @Author yo_hack
 * @Date 2021.11.21
 * @Description
 **/

const val TYPE_ALL = 1
const val TYPE_NORMAL = 2

class GameTnmtTabTitleEntity(val name: String?, val type: Int) : MultiItemEntity {
    override val itemType: Int
        get() = type
}

class GameTnmtTabTitleAdapter :
    BaseMultiItemQuickAdapter<GameTnmtTabTitleEntity, BaseViewHolder>() {

    var selectPos = 0


    init {
        addItemType(TYPE_ALL, R.layout.item_tab_game_tnmt_all)
        addItemType(TYPE_NORMAL, R.layout.item_tab_game_tnmt_normal)

//        setDiffCallback(object : DiffUtil.ItemCallback<GameTnmtTabTitleEntity>() {
//            override fun areItemsTheSame(
//                oldItem: GameTnmtTabTitleEntity,
//                newItem: GameTnmtTabTitleEntity
//            ): Boolean = oldItem.type == newItem.type
//
//            override fun areContentsTheSame(
//                oldItem: GameTnmtTabTitleEntity,
//                newItem: GameTnmtTabTitleEntity
//            ): Boolean = false
//
//        })
    }


    override fun convert(holder: BaseViewHolder, item: GameTnmtTabTitleEntity) {
        holder.setText(R.id.tv_title, item.name)
            .itemView.setBackgroundResource(
                if (holder.layoutPosition == selectPos) {
                    R.drawable.rect18_blue4870ff
                } else {
                    R.drawable.rect18_black21283f
                }
            )
    }
}