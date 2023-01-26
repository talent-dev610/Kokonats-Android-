package com.biiiiit.kokonats.ui.user.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.biiiiit.kokonats.databinding.FragmentUserInfoBinding
import com.biiiiit.kokonats.ui.game.ui.adapter.UserPlayHistoryAdapter
import com.biiiiit.kokonats.ui.user.vm.UserEnergyViewModel
import com.biiiiit.kokonats.ui.user.vm.UserInfoViewModel
import com.biiiiit.kokonats.ui.user.vm.UserKokoViewModel
import com.biiiiit.kokonats.utils.loadCircleAvatar
import com.biiiiit.kokonats.utils.setCommonRefreshLayout
import com.biiiiit.lib_base.base.BaseFragment
import com.biiiiit.lib_base.user.removeUserAndToken
import com.biiiiit.lib_base.utils.*
import com.biiiiit.lib_base.utils.photo.loadCircle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

/**
 * @Author yo_hack
 * @Date 2021.10.23
 * @Description 用户信息 fragment
 **/
class UserInfoFragment : BaseFragment<FragmentUserInfoBinding, UserInfoViewModel>() {


    private val energyVM: UserEnergyViewModel by lazy {
        getAppViewModel(UserEnergyViewModel::class.java)
    }

    private val kokoVM: UserKokoViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserKokoViewModel::class.java)
    }

    override val vm: UserInfoViewModel
        get() = ViewModelProvider(requireActivity()).get(getVMClazz())

    private val historyAdapter = UserPlayHistoryAdapter()

    override fun initView1() {
        binding.refreshLayout.setOnRefreshListener {
            actionAlways()
            actionOnce()
        }
        binding.ivEnergyAdd.setOnClickListener {
            jump2Main(context, 0)
        }
        binding.tvMore.setOnClickListener {
            simpleJump(ROUTE_TNMT_HISTORY, context)
        }
        binding.tvLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            context?.let {
                // exit client
                GoogleSignIn.getClient(it, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
            }
            removeUserAndToken()
            simpleJump(ROUTE_LOGIN, context)
            activity?.onBackPressed()
        }
        binding.ivSetting.setOnClickListener {
            simpleJump(ROUTE_USER_INFO_EDIT, context)
        }
        initRcvHistory()
    }

    private fun initRcvHistory() {
        binding.rcvPlays.layoutManager = LinearLayoutManager(context)
        binding.rcvPlays.adapter = historyAdapter
    }

    override fun initViewModel2() {
        setCommonRefreshLayout(binding.refreshLayout)
        vm.loginUser.observe(viewLifecycleOwner) {
            it?.let { user ->
                binding.apply {
                    tvName.text = user.userName
                    tvUid.text = "UID:${user.id}"
                    ivAvatar.loadCircle(user.picture, null, null)
                    bindAvatar(user.picture)
                }
            }
        }

        vm.tnmtPlayState.observe(viewLifecycleOwner) {
            parseState(it, {
                finishRefresh()
                historyAdapter.setNewInstance(it.toMutableList())
                visiblePlayHistory()
            }, {
                finishRefresh()
                visiblePlayHistory()
            })
        }

        energyVM.energy.observe(viewLifecycleOwner) {
            binding.tvEnergy.text = it.toString()
        }

        kokoVM.koko.observe(viewLifecycleOwner) {
            binding.tvReward.text = it.toPlainString()
        }


    }

    private fun bindAvatar(picture: String) {
        loadCircleAvatar(binding.ivAvatar, picture, context)
    }

    private fun visiblePlayHistory() {
        val empty = historyAdapter.data.isNullOrEmpty()
//        binding.tvMore.isVisible = !empty
    }


    private fun finishRefresh() {
        binding.refreshLayout.finishRefresh(0, true, true)
    }

    override fun actionAlways() {
        vm.queryUserInfo()
        energyVM.requestEnergy()
        kokoVM.requestKoko()
    }

    override fun actionOnce() {
        vm.queryUserTnmtPlayHistory()
    }

    override fun onDestroyView() {
        binding.rcvPlays.adapter = null
        super.onDestroyView()
    }


    override fun getVMClazz(): Class<UserInfoViewModel> = UserInfoViewModel::class.java

    override fun createBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentUserInfoBinding = FragmentUserInfoBinding.inflate(layoutInflater, container, false)
}