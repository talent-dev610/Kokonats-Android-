package com.biiiiit.kokonats.ui.chat.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.biiiiit.kokonats.databinding.FragmentChatDmMainBinding
import com.biiiiit.kokonats.ui.chat.TYPE_CHAT_DM
import com.biiiiit.kokonats.ui.chat.vm.ChatMainViewModel
import com.biiiiit.lib_base.base.BaseFragment

/**
 * created by Xiande.Min on 2022/4/3
 * content:  chat dm main module
 * 1、
 * 2、
 */
class ChatDMMainFragment : BaseFragment<FragmentChatDmMainBinding, ChatMainViewModel>() {

    override val vm: ChatMainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(TYPE_CHAT_DM, ChatMainViewModel::class.java)
    }

    companion object {
        fun newInstance() = ChatDMMainFragment()
    }

    override fun getVMClazz(): Class<ChatMainViewModel> = ChatMainViewModel::class.java

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentChatDmMainBinding.inflate(inflater, container, false)
}

