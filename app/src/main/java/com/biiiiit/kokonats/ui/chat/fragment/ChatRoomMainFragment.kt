package com.biiiiit.kokonats.ui.chat.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.biiiiit.kokonats.databinding.FragmentChatRoomMainBinding
import com.biiiiit.kokonats.ui.chat.TYPE_CHAT_PUBLIC
import com.biiiiit.kokonats.ui.chat.vm.ChatMainViewModel
import com.biiiiit.lib_base.base.BaseFragment

/**
 * @Author yo_hack
 * @Date 2022.03.29
 * @Description chat room fragment
 **/
class ChatRoomMainFragment : BaseFragment<FragmentChatRoomMainBinding, ChatMainViewModel>() {


    override val vm: ChatMainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(TYPE_CHAT_PUBLIC, ChatMainViewModel::class.java)
    }


    companion object {
        fun newInstance() = ChatRoomMainFragment()
    }

    override fun getVMClazz(): Class<ChatMainViewModel> = ChatMainViewModel::class.java

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentChatRoomMainBinding.inflate(inflater, container, false)
}