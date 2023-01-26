package com.biiiiit.kokonats.ui.chat.vm

import androidx.lifecycle.MutableLiveData
import com.biiiiit.kokonats.data.bean.ChatAuthor
import com.biiiiit.lib_base.base.BaseViewModel

/**
 * @Author yo_hack
 * @Date 2022.04.10
 * @Description share main viewModel
 **/
class ChatMainShareViewModel : BaseViewModel() {


    /**
     * 去 DM 或 cs 页面
     */
    var dmOrCSUser = MutableLiveData<ChatAuthor>()


}