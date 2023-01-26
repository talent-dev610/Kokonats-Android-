package com.biiiiit.kokonats.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.biiiiit.kokonats.databinding.FragmentHomeStoreBinding
import com.biiiiit.kokonats.ui.user.vm.UserEnergyViewModel
import com.biiiiit.kokonats.ui.game.viewmodel.HomeGameViewModel
import com.biiiiit.lib_base.base.BaseFragment
import com.biiiiit.lib_base.data.COMMON_DATA
import com.biiiiit.lib_base.utils.ROUTE_STORE_ENERGY
import com.biiiiit.lib_base.utils.ROUTE_STORE_GAME
import com.biiiiit.lib_base.utils.simpleJump
import com.biiiiit.lib_base.utils.simpleJumpWith
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener

/**
 * @Author yo_hack
 * @Date 2021.11.05
 * @Description 首页商店
 **/
class HomeStoreFragment : BaseFragment<FragmentHomeStoreBinding, HomeStoreViewModel>() {

    override val vm: HomeStoreViewModel
        get() = ViewModelProvider(requireActivity()).get(getVMClazz())

    private val gameAdapter = StoreGameAdapter()

    private val energyVM: UserEnergyViewModel by lazy {
        getAppViewModel(UserEnergyViewModel::class.java)
    }

    private val gameVM: HomeGameViewModel by lazy {
        getAppViewModel(HomeGameViewModel::class.java)
    }


    override fun initView1() {

        binding.refreshLayout.setOnRefreshListener {
            actionAlways()
            actionOnce()
        }

        binding.btnBuyEnergy.setOnClickListener(View.OnClickListener {
            simpleJump(ROUTE_STORE_ENERGY, context)
        })

        gameAdapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->  gameAdapter.getItemOrNull(position)?.let {
            simpleJumpWith(ROUTE_STORE_GAME, context, Bundle().apply {
                putSerializable(COMMON_DATA, it)
            })
        }})
        binding.rcvItems.layoutManager = LinearLayoutManager(context)
        binding.rcvItems.adapter = gameAdapter
    }

    override fun initViewModel2() {
        vm.stopRetryConsume = false

        vm.hideRefresh.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.refreshLayout.finishRefresh(0)
                vm.hideRefresh.value = null
            }
        }
        vm.buySkuDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                vm.buySkuDetails.value = null
                vm.launchPayIntent(requireActivity(), it)
            }
        }
        vm.purchaseEvent.observe(viewLifecycleOwner) {
            if (it == true) {
                energyVM.requestEnergy()
                vm.purchaseEvent.value = null
            }
        }

        energyVM.energy.observe(viewLifecycleOwner) {
            binding.energyView.setNewEnergyLong(it)
        }

        gameVM.gameWin.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvCongratulations.text = it
                binding.tvCongratulations.isSelected = true
            }
        }

        gameVM.gameList.observe(viewLifecycleOwner) {
            gameAdapter.setNewInstance(it)
        }
    }

    override fun actionOnce() {
        gameVM.generateReward(resources)
        gameVM.queryGame()
    }

    override fun actionAlways() {
        energyVM.requestEnergy()
    }

    override fun onDestroyView() {
        vm.stopRetryConsume = true
        super.onDestroyView()
    }

    override fun getVMClazz(): Class<HomeStoreViewModel> = HomeStoreViewModel::class.java

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeStoreBinding.inflate(inflater, container, false)
}