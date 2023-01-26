package com.biiiiit.kokonats.ui.chat.adapter

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.ChatMessage
import com.biiiiit.kokonats.utils.loadCircleAvatar
import com.biiiiit.lib_base.user.getUser
import com.biiiiit.lib_base.utils.dp2px
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author yo_hack
 * @Date 2022.03.27
 * @Description 聊天详情 item  text
 **/
class ChatDetailTextAdapter : BaseQuickAdapter<ChatMessage, BaseViewHolder>(R.layout.item_chat_detail_text) {

    private val user = getUser()
    private val pixel = dp2px(null, 6)

    private val dFormat = SimpleDateFormat("MM-dd hh:mm", Locale.getDefault())
    private val sFormat = SimpleDateFormat("hh:mm", Locale.getDefault())

    init {
        setDiffCallback(object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage) =
                oldItem.sentAt == newItem.sentAt &&
                    oldItem.author?.id == newItem.author?.id &&
                    oldItem.body == newItem.body

            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage) =
                areItemsTheSame(oldItem, newItem)

        })
    }


    override fun convert(holder: BaseViewHolder, item: ChatMessage) {
        val isMe = item.author?.id == user?.id

        val gs = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = pixel.toFloat()
            setColor(ContextCompat.getColor(context, if (isMe) {
                R.color.yellow_88ff9900
            } else {
                R.color.black_323755
            }))
        }

        val time = item.sentAt?.seconds?.let {it->
            val t = it * 1000
            val instance = Calendar.getInstance()
            val sendAt = Calendar.getInstance().apply { timeInMillis = t }

            if (instance.get(Calendar.YEAR) == sendAt.get(Calendar.YEAR) &&
                instance.get(Calendar.DAY_OF_YEAR) == sendAt.get(Calendar.DAY_OF_YEAR)) {
                // same day
                sFormat.format(t)
            } else {
                dFormat.format(t)
            }
        } ?: kotlin.run {
            ""
        }

        holder.setText(R.id.tv_name, item.author?.userName)
            .setText(R.id.tv_time, time)
            .setText(R.id.tv_content, item.body)
        holder.getView<View>(R.id.tv_content).background = gs
        loadCircleAvatar(holder.getView(R.id.iv_avatar), item.author?.picture ?: "0", context)
    }
}