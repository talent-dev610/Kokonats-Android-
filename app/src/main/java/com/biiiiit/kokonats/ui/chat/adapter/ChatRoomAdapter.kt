package com.biiiiit.kokonats.ui.chat.adapter

import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.ChatThread
import com.biiiiit.lib_base.utils.photo.loadCropRound
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @Author yo_hack
 * @Date 2022.03.27
 * @Description 聊天室 main room
 **/
class ChatRoomAdapter : BaseQuickAdapter<ChatThread, BaseViewHolder>(R.layout.item_chat_room) {

    init {
        setDiffCallback(object : DiffUtil.ItemCallback<ChatThread>() {
            override fun areItemsTheSame(oldItem: ChatThread, newItem: ChatThread) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ChatThread, newItem: ChatThread) =
                oldItem.name == newItem.name &&
                    oldItem.type == newItem.type &&
                    oldItem.lastSentAt == newItem.lastSentAt
        })
    }

    override fun convert(holder: BaseViewHolder, item: ChatThread) {
        holder.setText(R.id.tv_title, item.name)
        holder.getView<ImageView>(R.id.iv_avatar)
            .loadCropRound(item.icon, 10, null, null)
    }
}