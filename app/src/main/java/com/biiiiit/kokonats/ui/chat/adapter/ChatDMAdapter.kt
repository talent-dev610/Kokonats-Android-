package com.biiiiit.kokonats.ui.chat.adapter

import androidx.recyclerview.widget.DiffUtil
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.ChatThread
import com.biiiiit.kokonats.utils.loadCircleAvatar
import com.biiiiit.lib_base.user.getUser
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @Author yo_hack
 * @Date 2022.03.27
 * @Description 用户 user
 **/
class ChatDMAdapter : BaseQuickAdapter<ChatThread, BaseViewHolder>(R.layout.item_chat_user) {

    private val user = getUser()

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

        val other = item.membersDetail?.find { it.id != user?.id }
            ?: item.membersDetail?.firstOrNull()
        holder.setText(R.id.tv_title, other?.userName)
        loadCircleAvatar(holder.getView(R.id.iv_avatar), other?.picture ?: "0", context)
    }
}