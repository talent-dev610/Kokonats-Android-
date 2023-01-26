package com.biiiiit.kokonats.ui.chat.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.CallSuper
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.biiiiit.kokonats.data.bean.ChatMessage
import com.biiiiit.kokonats.databinding.FragmentChatDmListBinding
import com.biiiiit.kokonats.ui.chat.adapter.ChatDetailTextAdapter
import com.biiiiit.kokonats.ui.chat.vm.ChatMainViewModel
import com.biiiiit.lib_base.base.BaseFragment

/**
 * @Author yo_hack
 * @Date 2022.04.05
 * @Description chat dm list fragment
 **/
abstract class AbsChatListFragment : BaseFragment<FragmentChatDmListBinding, ChatMainViewModel>() {


    protected var adapter = ChatDetailTextAdapter()


    @CallSuper
    override fun initView1() {
        initRecyclerView()

        binding.ivSend.setOnClickListener {
            sendMessage()
        }

        binding.etMsg.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
                true
            } else {
                false
            }
        }

        binding.ivUp.setOnClickListener {
            NavHostFragment.findNavController(this).navigateUp()
        }
    }

    @CallSuper
    override fun initViewModel2() {
        vm.details.observe(viewLifecycleOwner) {
            val isEmpty = adapter.data.isNullOrEmpty()
            val isBottom =
                if (isEmpty) {
                    (binding.rcvContent.layoutManager as? LinearLayoutManager)
                        ?.findLastVisibleItemPosition() == adapter.data.size
                } else {
                    false
                }
            adapter.setNewInstance(it)
            if (isEmpty || isBottom) {
                binding.rcvContent.scrollToPosition(it.size - 1)
            }
        }
        vm.sendMsgData.observe(viewLifecycleOwner) {
            if (!it?.first.isNullOrBlank()) {
                binding.etMsg.setText("")
            }
        }
    }

    private fun sendMessage() {
        val msg = binding.etMsg.text.toString().trim()
        if (!msg.isNullOrBlank()) {
            doSendMessage(msg)
        }
    }

    abstract fun doSendMessage(msg: String)

    override fun onDestroyView() {
        adapter.data.clear()
        binding.rcvContent.adapter = null
        vm.detailRegistration?.remove()
        super.onDestroyView()
    }

    override fun onDestroy() {
        vm.details.value = mutableListOf()
        super.onDestroy()
    }

    protected open fun initRecyclerView() {
        binding.rcvContent.layoutManager = LinearLayoutManager(context)
        binding.rcvContent.adapter = adapter
    }

    override fun getVMClazz(): Class<ChatMainViewModel> = ChatMainViewModel::class.java

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentChatDmListBinding.inflate(inflater, container, false)
}