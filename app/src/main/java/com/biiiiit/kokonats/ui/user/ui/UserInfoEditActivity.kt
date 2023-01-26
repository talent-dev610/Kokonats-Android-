package com.biiiiit.kokonats.ui.user.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.biiiiit.kokonats.databinding.ActivityUserInfoEditBinding
import com.biiiiit.kokonats.ui.user.vm.UserInfoViewModel
import com.biiiiit.kokonats.utils.loadCircleAvatar
import com.biiiiit.lib_base.base.BaseActivity
import com.biiiiit.lib_base.utils.ROUTE_USER_INFO_EDIT
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener

/**
 * @Author yo_hack
 * @Date 2022.03.25
 * @Description user info edit info
 **/
@Route(path = ROUTE_USER_INFO_EDIT)
class UserInfoEditActivity : BaseActivity<ActivityUserInfoEditBinding, UserInfoViewModel>() {

    private var adapter = UserInfoEditAvatarAdapter()

    override fun initView1() {
        binding.rcvContent.layoutManager = GridLayoutManager(this, 5)
        binding.rcvContent.adapter = adapter
        adapter.setNewInstance((1..5).map { it.toString() }.toMutableList())
        adapter.setOnItemClickListener { _, _, pos ->
            setPictureSelected((pos + 1).toString())
        }


        binding.ivBack.setOnClickListener { onBackPressed() }
        binding.tvOk.setOnClickListener {
            val name = binding.etNick.text.toString().trim()
            vm.changeUserInfo(adapter.mCheckStr, name)
        }
    }

    override fun initViewModel2() {
        vm.loginUser.observe(this) { user ->
            binding.etNick.setText(user.userName)
            binding.tvUid.text = "UID:${user.id}"
            setPictureSelected(user.picture)
        }

        vm.changeSuccess.observe(this) {
            finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setPictureSelected(str: String) {
        adapter.mCheckStr = str
        loadCircleAvatar(binding.ivAvatar, str, this)
        adapter.notifyDataSetChanged()
    }


    override fun getVMClazz(): Class<UserInfoViewModel> = UserInfoViewModel::class.java

    override fun createBinding(layoutInflater: LayoutInflater): ActivityUserInfoEditBinding =
        ActivityUserInfoEditBinding.inflate(layoutInflater)
}