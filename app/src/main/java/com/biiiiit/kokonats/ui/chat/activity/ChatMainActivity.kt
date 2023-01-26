package com.biiiiit.kokonats.ui.chat.activity

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.biiiiit.kokonats.databinding.ActivityChatMainBinding
import com.biiiiit.kokonats.ui.chat.fragment.ChatCSFragment
import com.biiiiit.kokonats.ui.chat.fragment.ChatDMMainFragment
import com.biiiiit.kokonats.ui.chat.fragment.ChatRoomMainFragment
import com.biiiiit.kokonats.ui.chat.vm.ChatMainShareViewModel
import com.biiiiit.kokonats.ui.chat.vm.ChatMainViewModel
import com.biiiiit.lib_base.base.BaseActivity
import com.biiiiit.lib_base.utils.ROUTE_CHAT_MAIN

/**
 * @Author yo_hack
 * @Date 2022.03.28
 * @Description  chat 聊天main activity
 **/
@Route(path = ROUTE_CHAT_MAIN)
class ChatMainActivity : BaseActivity<ActivityChatMainBinding, ChatMainViewModel>() {

    private val shareVM by viewModels<ChatMainShareViewModel>()

    override fun initView1() {
        super.initView1()
        binding.ivCancel.setOnClickListener { finish() }

        binding.tvRoom.setOnClickListener { setVpCurrentItem(0) }
        binding.tvUser.setOnClickListener { setVpCurrentItem(1) }
        binding.tvCustomer.setOnClickListener { setVpCurrentItem(2) }

        binding.viewpager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 3

            override fun createFragment(position: Int): Fragment = when (position) {
                1 -> ChatDMMainFragment.newInstance()
                2 -> ChatCSFragment.newInstance()
                else -> ChatRoomMainFragment.newInstance()
            }
        }
        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tvRoom.isSelected = position == 0
                binding.tvUser.isSelected = position == 1
                binding.tvCustomer.isSelected = position == 2
            }
        })
        binding.tvRoom.performClick()
        binding.viewpager.isUserInputEnabled = false
    }

    override fun initViewModel2() {
        super.initViewModel2()
        shareVM.dmOrCSUser.observe(this) {
            if (it == null || it.id < 0) {
                return@observe
            }
            if (it.id == 0L) {
                binding.viewpager.currentItem = 2
                shareVM.dmOrCSUser.value = null
            } else if (it.id > 0) {
                binding.viewpager.currentItem = 1
            }
        }
    }

    override val showFloat: Boolean
        get() = false

    private fun setVpCurrentItem(pos: Int) {
        binding.viewpager.currentItem = pos
    }

    override fun getVMClazz(): Class<ChatMainViewModel> = ChatMainViewModel::class.java

    override fun createBinding(layoutInflater: LayoutInflater) =
        ActivityChatMainBinding.inflate(layoutInflater)
}