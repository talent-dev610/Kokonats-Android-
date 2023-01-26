package com.biiiiit.kokonats.ui.user.ui

import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.utils.loadCircleAvatar
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @Author yo_hack
 * @Date 2022.03.26
 * @Description
 **/
class UserInfoEditAvatarAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_user_info_edit_avatar) {

    var mCheckStr = ""

    override fun convert(holder: BaseViewHolder, item: String) {
        loadCircleAvatar(holder.getView(R.id.iv_avatar), item, context, item == mCheckStr)
    }
}
