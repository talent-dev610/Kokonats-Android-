package com.biiiiit.kokonats.ui.game.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.biiiiit.kokonats.databinding.FragmentTnmtResultBinding
import com.biiiiit.kokonats.ui.game.ui.adapter.TnmtResultAdapter
import com.biiiiit.kokonats.ui.game.viewmodel.TnmtResultViewModel
import com.biiiiit.lib_base.base.BaseFragment
import com.biiiiit.lib_base.data.COMMON_DATA
import com.biiiiit.lib_base.data.COMMON_ID

/**
 * @Author yo_hack
 * @Date 2022.01.13
 * @Description tnmt result
 **/
class TnmtResultFragment : BaseFragment<FragmentTnmtResultBinding, TnmtResultViewModel>() {


    private var score: String = ""

    /**
     * tnmt --> tnmtId
     * pvp --> matchPlayId
     */
    private var id = 0L

    private val adapter = TnmtResultAdapter()


    companion object {
        fun newInstance(id: Long, score: String) = TnmtResultFragment().apply {
            arguments = Bundle().apply {
                putLong(COMMON_ID, id)
                putString(COMMON_DATA, score)
            }
        }
    }

    override fun beforeOnCreate0() {
        arguments?.let {
            score = it.getString(COMMON_DATA, "")
            id = it.getLong(COMMON_ID, 0)
        }
    }

    override fun initView1() {
        binding.tvScore.text = "+${score}"
        binding.rcvScore.layoutManager = LinearLayoutManager(context)
        binding.rcvScore.adapter = adapter

        binding.tvBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun initViewModel2() {
        vm.tnmtPlayList.observe(this) {
            adapter.setNewInstance(it)
        }
    }

    override fun actionAlways() {
        vm.queryTnmtPlayHistory(id)
    }

    override fun getVMClazz(): Class<TnmtResultViewModel> = TnmtResultViewModel::class.java

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentTnmtResultBinding.inflate(inflater, container, false)
}