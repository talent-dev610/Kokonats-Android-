package com.biiiiit.kokonats.ui.chat.fragment

import android.view.View
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.ChatAuthor
import com.biiiiit.kokonats.data.bean.ChatThread
import com.biiiiit.kokonats.ui.chat.TYPE_CHAT_CS
import com.biiiiit.kokonats.ui.chat.vm.ChatMainViewModel
import com.biiiiit.lib_base.utils.dp2px
import com.biiiiit.lib_base.utils.logi


/**
 * @Author yo_hack
 * @Date 2022.04.05
 * @Description chat cs fragment
 **/
class ChatCSFragment : AbsChatListFragment() {

    override val vm: ChatMainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(TYPE_CHAT_CS, ChatMainViewModel::class.java)
    }


    companion object {
        fun newInstance() = ChatCSFragment()
    }

    override fun initView1() {
        super.initView1()
        binding.ivUp.visibility = View.GONE
        val horizontal = resources.getDimensionPixelSize(R.dimen.common_margin_53)
        binding.rcvContent.updatePadding(left = horizontal, right = horizontal)
    }

    override fun initViewModel2() {
        super.initViewModel2()

        vm.titles.observe(viewLifecycleOwner) {
            val thread = it.firstOrNull()
            if (thread != null) {
                vm.currentThread = thread
                if (adapter.data.isNullOrEmpty()) {
                    vm.queryDataListById(thread.id)
                }
            } else {
                vm.currentUser.value = ChatAuthor().apply {
                    this.id = 0
                    this.userName = "KOKO_CS_1"
                    this.picture = "1"
                }
            }
        }

    }

    override fun doSendMessage(msg: String) {
        vm.sendCSMsg(msg)
    }

    override fun actionOnce() {
        super.actionOnce()
        vm.queryType(TYPE_CHAT_CS)
    }
}