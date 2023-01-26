package com.biiiiit.kokonats.ui.chat.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.biiiiit.kokonats.ui.chat.TYPE_CHAT_DM
import com.biiiiit.kokonats.ui.chat.vm.ChatMainViewModel
import com.biiiiit.lib_base.user.getUser

/**
 * @Author yo_hack
 * @Date 2022.03.29
 * @Description chat dm detail fragment
 **/
class ChatDMDetailFragment : AbsChatListFragment() {

    override val vm: ChatMainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(TYPE_CHAT_DM, ChatMainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this) {
            NavHostFragment.findNavController(this@ChatDMDetailFragment).popBackStack()
        }
    }

    override fun initView1() {
        super.initView1()
        binding.ivUp.visibility = View.VISIBLE
        var name = vm.currentUser.value?.userName
        if (name.isNullOrBlank()) {
            name = vm.currentThread.membersDetail?.find { it.id != getUser()?.id }?.userName
        }
        binding.tvTitle.text = name
    }

    override fun actionOnce() {
        super.actionOnce()
        vm.queryDataListById(vm.currentThread.id)
    }

    override fun doSendMessage(msg: String) {
        vm.sendDMMsg(msg)
    }

}