package com.biiiiit.kokonats.ui.store

import android.view.LayoutInflater
import android.view.ViewGroup
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.databinding.FragmentFakeStoreBinding
import com.biiiiit.lib_base.base.BaseFragment
import com.biiiiit.lib_base.base.EmptyViewModel

/**
 * @Author yo_hack
 * @Date 2021.12.07
 * @Description
 **/
class FakeStoreFragment : BaseFragment<FragmentFakeStoreBinding, EmptyViewModel>() {
    override fun getVMClazz(): Class<EmptyViewModel> = EmptyViewModel::class.java

    override fun createBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentFakeStoreBinding = FragmentFakeStoreBinding.inflate(inflater, container, false)

    override fun initView1() {

        binding.sponsorView.setSponsorData(
            R.drawable.ic_def_store_top,
            "https://game.kokonats.club/",
            "Koko Club"
        )
        super.initView1()
    }
}