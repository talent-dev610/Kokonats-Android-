package com.biiiiit.kokonats.ui.chat.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.ChatAuthor
import com.biiiiit.kokonats.data.bean.ChatThread
import com.biiiiit.kokonats.databinding.FragmentChatRoomTitleBinding
import com.biiiiit.kokonats.ui.chat.TYPE_CHAT_DM
import com.biiiiit.kokonats.ui.chat.adapter.ChatDMAdapter
import com.biiiiit.kokonats.ui.chat.vm.ChatMainShareViewModel
import com.biiiiit.kokonats.ui.chat.vm.ChatMainViewModel
import com.biiiiit.lib_base.base.BaseFragment

/**
 * @Author yo_hack
 * @Date 2022.03.29
 * @Description chat dm titles
 **/
class ChatDMTitleFragment : BaseFragment<FragmentChatRoomTitleBinding, ChatMainViewModel>() {

    private var adapter = ChatDMAdapter()

    private val shareVM by activityViewModels<ChatMainShareViewModel>()

    override val vm: ChatMainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(TYPE_CHAT_DM, ChatMainViewModel::class.java)
    }


    override fun initView1() {
        super.initView1()

        binding.etRoom.hint = resources.getString(R.string.chat_search_dm)
        binding.rcvContent.layoutManager = LinearLayoutManager(context)
        binding.rcvContent.adapter = adapter

        adapter.setOnItemClickListener { _, _, pos ->
            adapter.getItemOrNull(pos)?.let {
                nav2DMDetail(it)
            }
        }

        binding.etRoom.doAfterTextChanged {
            val str = it.toString().trim()
            vm.titles.value?.let {
                if (str.isNullOrBlank()) {
                    adapter.setNewInstance(it.toMutableList())
                } else {
                    it.filter { it.name.contains(str, true) }.toMutableList().let {
                        adapter.setNewInstance(it)
                    }
                }
            }
        }
    }

    /**
     * 跳转到详情
     */
    private fun nav2DMDetail(thread: ChatThread?) {
        thread?.let { vm.currentThread = thread }
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_chat_dm_title_detail, null, null,
                FragmentNavigator.Extras.Builder()
                    .addSharedElement(binding.etRoom, resources.getString(R.string.transition_chat))
                    .build())
    }

    override fun initViewModel2() {
        super.initViewModel2()
        vm.titles.observe(viewLifecycleOwner) {
            adapter.setNewInstance(it.toMutableList())
            toNavDetail(shareVM.dmOrCSUser.value, true)
        }

        shareVM.dmOrCSUser.observe(viewLifecycleOwner) { id ->
            toNavDetail(id, false)
        }
    }

    /**
     * 跳转到详情
     */
    private fun toNavDetail(chatAuthor: ChatAuthor?, force: Boolean) {
        if (chatAuthor == null || chatAuthor.id < 0) {
            return
        }

        adapter.data.find { it.members?.contains(chatAuthor.id) ?: false }?.let {
            nav2DMDetail(it)
            shareVM.dmOrCSUser.value = null
        } ?: kotlin.run {
            if (force) {
                vm.currentUser.value = chatAuthor
                nav2DMDetail(null)
                shareVM.dmOrCSUser.value = null
            }
        }
    }

    override fun actionOnce() {
        super.actionOnce()
        vm.queryType(TYPE_CHAT_DM)
    }


    override fun getVMClazz(): Class<ChatMainViewModel> = ChatMainViewModel::class.java

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentChatRoomTitleBinding.inflate(inflater, container, false)
}