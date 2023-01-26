package com.biiiiit.kokonats.ui.game.ui.adapter

import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.Game
import com.biiiiit.lib_base.BaseApp
import com.biiiiit.lib_base.utils.dp2px
import com.biiiiit.lib_base.utils.getScreenWidth
import com.biiiiit.lib_base.utils.photo.loadCropRound
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @Author yo_hack
 * @Date 2021.10.31
 * @Description 首页 gameTab  -> game
 **/
class HomeGameGameAdapter :
    BaseQuickAdapter<Game, BaseViewHolder>(R.layout.item_home_game_tab_game) {

    var singleWidth =
        getScreenWidth(null) - BaseApp.app.resources.getDimensionPixelSize(R.dimen.common_margin_53) -
            82.dp2px(null)

    init {
        setDiffCallback(object : DiffUtil.ItemCallback<Game>() {
            override fun areItemsTheSame(
                oldItem: Game, newItem: Game
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
                return oldItem.coverImageUrl == newItem.coverImageUrl &&
                    oldItem.name == newItem.name
            }
        })
    }

    override fun convert(holder: BaseViewHolder, item: Game) {
        holder.itemView.layoutParams = holder.itemView.layoutParams.apply {
            this.width = singleWidth
        }
        holder.setText(R.id.tv_title, item.name)

        holder.getView<ImageView>(R.id.iv_cover).loadCropRound(item.coverImageUrl, 9, 9, 0, 0, 0, 0)

    }
}