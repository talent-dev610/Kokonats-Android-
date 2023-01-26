package com.biiiiit.kokonats.ui.store.energy

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.BaseAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.biiiiit.kokonats.databinding.ActivityStoreEnergyBinding
import com.biiiiit.kokonats.ui.store.HomeStoreViewModel
import com.biiiiit.kokonats.ui.user.vm.UserEnergyViewModel
import com.biiiiit.lib_base.adapter.OnGridItemClickListener
import com.biiiiit.lib_base.base.BaseActivity
import com.biiiiit.lib_base.net.KOKO_GAME_URL
import com.biiiiit.lib_base.utils.ROUTE_STORE_ENERGY
import com.biiiiit.lib_base.utils.logi

/**
 * @Author Lucas Jordan
 * @Date 2022.04.13
 * @Description energy store
 **/

@Route(path = ROUTE_STORE_ENERGY)
class StoreEnergyActivity : BaseActivity<ActivityStoreEnergyBinding, StoreEnergyViewModel>() {

    private val energyVM: UserEnergyViewModel by lazy {
        getAppViewModel(UserEnergyViewModel::class.java)
    }

    override fun initView1() {
        super.initView1()
        binding.ivBack.setOnClickListener(View.OnClickListener { finish() })
        binding.tvEnergyUrl.text = KOKO_GAME_URL
        binding.cvEnergy.setOnClickListener(View.OnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(KOKO_GAME_URL)))
        })
        binding.ivEnergyCopy.setOnClickListener(View.OnClickListener {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("koko_energy_url", KOKO_GAME_URL)
            clipboardManager.setPrimaryClip(clipData)
            showToast("URL copied to clipboard")
        })
    }

    override fun initViewModel2() {
        super.initViewModel2()
        energyVM.energy.observe(getLOwner()) {
            binding.tvEnergy.text = "%,d".format(it)
        }
    }

    override fun actionAlways() {
        super.actionAlways()
        energyVM.requestEnergy()
    }

    override fun getVMClazz(): Class<StoreEnergyViewModel> = StoreEnergyViewModel::class.java

    override fun createBinding(layoutInflater: LayoutInflater): ActivityStoreEnergyBinding = ActivityStoreEnergyBinding.inflate(layoutInflater)
}