package com.biiiiit.kokonats.ui.user.ui

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.databinding.BaseLoadingRecyclerBinding
import com.biiiiit.kokonats.databinding.PopHistoryGameTitleBinding
import com.biiiiit.kokonats.ui.game.ui.adapter.UserPlayHistoryAdapter
import com.biiiiit.kokonats.ui.user.vm.UserKokoViewModel
import com.biiiiit.kokonats.ui.user.vm.UserTnmtHistoryViewModel
import com.biiiiit.lib_base.base.BaseFragment
import com.biiiiit.lib_base.utils.dp2px
import com.biiiiit.lib_base.utils.setListObserver

/**
 * @Author yo_hack
 * @Date 2021.12.01
 * @Description 用户 tnmt history
 **/
class UserTnmtHistoryFragment :
    BaseFragment<BaseLoadingRecyclerBinding, UserTnmtHistoryViewModel>() {

    private val historyAdapter = UserPlayHistoryAdapter()

    private var tvReward: TextView? = null
    private var tvAll: TextView? = null
    private var llAll: View? = null

    private val kokoVM: UserKokoViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserKokoViewModel::class.java)
    }

    companion object {
        fun newInstance() = UserTnmtHistoryFragment()
    }

    override fun initView1() {
        binding.headerEnergy.setBackClick { activity?.onBackPressed() }
        binding.headerEnergy.setNewEnergyLong(-1)

        binding.refreshLayout.setOnRefreshListener {
            tvAll?.text = vm.ALL
            actionOnce()
        }
        binding.refreshLayout.setOnLoadMoreListener {
            vm.loadList(false)
        }

        binding.rcvContent.layoutManager = LinearLayoutManager(context)
        binding.rcvContent.adapter = historyAdapter

        addHeader()
    }

    /**
     */
    private fun showPopupWindow() {
        val initTitlePair = vm.initTitlePair()
        if (initTitlePair.isNullOrEmpty().not()) {
            val pop = TnmtHistoryTitlePopupWindow(PopHistoryGameTitleBinding.inflate(layoutInflater), initTitlePair) {
                vm.selectedPos.value = it
            }
            pop.showAsDropDown(llAll, 0, dp2px(context, 1), Gravity.BOTTOM)
        }
    }

    private fun addHeader() {
        historyAdapter.removeAllHeaderView()
        historyAdapter.addHeaderView(
            layoutInflater.inflate(R.layout.header_tnmt_history, null).apply {
                tvReward = findViewById(R.id.tv_reward)
                tvAll = findViewById(R.id.tv_all)
                llAll = findViewById<View>(R.id.ll_all)
                llAll?.setOnClickListener { showPopupWindow() }
                setRewardText()
            })
        historyAdapter.headerWithEmptyEnable = true
    }

    override fun initViewModel2() {
        vm.ALL = getString(R.string.all)
        vm.data.observe(viewLifecycleOwner) {
            setListObserver(binding.refreshLayout, null, null, historyAdapter, it) { _, msg ->
                showToast(msg)
            }
        }

        vm.title.observe(viewLifecycleOwner) {
            tvAll?.text = it
        }


        kokoVM.koko.observe(viewLifecycleOwner) {
            setRewardText()
        }
    }

    private fun setRewardText() {
        kokoVM.koko.value?.let {
            tvReward?.text = "+${it.toPlainString()}"
        }
    }

    override fun onDestroyView() {
        tvReward = null
        super.onDestroyView()
    }

    override fun getVMClazz(): Class<UserTnmtHistoryViewModel> =
        UserTnmtHistoryViewModel::class.java

    override fun createBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): BaseLoadingRecyclerBinding =
        BaseLoadingRecyclerBinding.inflate(inflater, container, false)

    override fun actionOnce() {
        vm.loadList(true)
        kokoVM.requestKoko()
    }
}