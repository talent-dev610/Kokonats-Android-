package com.biiiiit.kokonats.ui.chat.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.ui.chat.TYPE_CHAT_PUBLIC
import com.biiiiit.kokonats.ui.chat.vm.ChatMainShareViewModel
import com.biiiiit.kokonats.ui.chat.vm.ChatMainViewModel
import com.biiiiit.lib_base.user.getUser


/**
 * @Author yo_hack
 * @Date 2022.03.29
 * @Description chat room detail fragment
 **/
class ChatRoomDetailFragment : AbsChatListFragment() {

    private val shareVM by activityViewModels<ChatMainShareViewModel>()

    override val vm: ChatMainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(TYPE_CHAT_PUBLIC, ChatMainViewModel::class.java)
    }


    override fun initRecyclerView() {
        binding.ivUp.visibility = View.VISIBLE
        super.initRecyclerView()
        adapter.addChildClickViewIds(R.id.iv_avatar)
        adapter.setOnItemClickListener { _, _, pos ->
            adapter.getItemOrNull(pos)?.author?.let { author ->
                val user = getUser()
                when (author.id) {
                    user?.id -> {
                        // 自己不和自己聊天咯
                    }
                    else -> {
                        // DM
                        shareVM.dmOrCSUser.value = author
                    }
                }
            }
        }
    }

    override fun initView1() {
        super.initView1()
        binding.tvTitle.text = vm.currentThread.name
    }

    override fun actionOnce() {
        super.actionOnce()
        vm.queryDataListById(vm.currentThread.id)
    }

    override fun doSendMessage(msg: String) {
        vm.sendPublicMsg(msg)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this) {
            NavHostFragment.findNavController(this@ChatRoomDetailFragment).popBackStack()
        }
    }
}