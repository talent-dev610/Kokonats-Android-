package com.biiiiit.kokonats.ui.chat.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.databinding.FragmentChatRoomTitleBinding
import com.biiiiit.kokonats.ui.chat.TYPE_CHAT_PUBLIC
import com.biiiiit.kokonats.ui.chat.adapter.ChatRoomAdapter
import com.biiiiit.kokonats.ui.chat.vm.ChatMainViewModel
import com.biiiiit.lib_base.base.BaseFragment

/**
 * @Author yo_hack
 * @Date 2022.03.29
 * @Description chat room titles
 **/
class ChatRoomTitleFragment : BaseFragment<FragmentChatRoomTitleBinding, ChatMainViewModel>() {

    private var adapter = ChatRoomAdapter()


    override val vm: ChatMainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(TYPE_CHAT_PUBLIC, ChatMainViewModel::class.java)
    }


    override fun initView1() {
        super.initView1()

        binding.etRoom.hint = resources.getString(R.string.chat_search_public)
        binding.rcvContent.layoutManager = LinearLayoutManager(context)
        binding.rcvContent.adapter = adapter

        adapter.setOnItemClickListener { _, _, pos ->
            adapter.getItemOrNull(pos)?.let {
                vm.currentThread = it
                NavHostFragment.findNavController(this)
                    .navigate(R.id.action_chat_room_title_detail, null, null,
                        FragmentNavigator.Extras.Builder()
                            .addSharedElement(binding.etRoom, resources.getString(R.string.transition_chat))
                            .build())

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

    override fun initViewModel2() {
        super.initViewModel2()
        vm.titles.observe(viewLifecycleOwner) {
            adapter.setNewInstance(it.toMutableList())
        }
    }

    override fun actionOnce() {
        super.actionOnce()
        vm.queryType(TYPE_CHAT_PUBLIC)
    }


    override fun getVMClazz(): Class<ChatMainViewModel> = ChatMainViewModel::class.java

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentChatRoomTitleBinding.inflate(inflater, container, false)
}