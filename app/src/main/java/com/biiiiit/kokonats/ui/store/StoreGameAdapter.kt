package com.biiiiit.kokonats.ui.store

import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.Game
import com.biiiiit.kokonats.data.bean.NetInAppProduct
import com.biiiiit.lib_base.utils.logi
import com.biiiiit.lib_base.utils.photo.loadCropRound
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @Author Lucas Jordan
 * @Date 2022.04.13
 * @Description
 **/
class StoreGameAdapter :
    BaseQuickAdapter<Game, BaseViewHolder>(R.layout.item_home_store_item) {

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
        holder.setText(R.id.tv_title, item.name)
        holder.getView<ImageView>(R.id.iv_cover).loadCropRound(item.iconUrl, 0, 0, 0)
    }
}