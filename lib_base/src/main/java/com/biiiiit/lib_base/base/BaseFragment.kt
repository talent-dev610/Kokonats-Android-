package com.biiiiit.lib_base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.biiiiit.lib_base.BaseApp

/**
 * @Author yo_hack
 * @Date 2021.10.15
 * @Description base fragment for this app
 **/
abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel>() :
    AbsFragment() {

    /** vm **/
    protected open val vm: VM by lazy {
        ViewModelProvider(this).get(getVMClazz())
    }

    private var _binding: VB? = null

    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    protected val binding get() = _binding!!


    protected var hasQuery = false

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeOnCreate0()
        recoverBundle(savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createBinding(inflater, container)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recoverBundle(savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        initView1()
        registerLoadAndToastEvent()
        initViewModel2()
        if (!hasQuery) {
            actionOnce()
            hasQuery = true
        }
    }

    override fun onResume() {
        super.onResume()
        actionAlways()
    }

    protected open fun recoverBundle(bundle: Bundle?) = Unit

    /**
     * method for query network or something
     */
    protected open fun actionOnce() = Unit

    /**
     * method for query network or something
     */
    protected open fun actionAlways() = Unit

    /**
     * get vm clazz
     */
    abstract fun getVMClazz(): Class<VM>

    /**
     * create binding
     */
    abstract fun createBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    /**
     * flow 0 1 2
     */
    open fun beforeOnCreate0() = Unit

    /**
     * method for init view, setOnClickListener etc..
     */
    open fun initView1() = Unit

    /**
     * method for init viewModel, vm.observe etc...
     */
    open fun initViewModel2() = Unit

    /**
     * register common viewModel
     * use for multi register
     */
    protected open fun registerLoadAndToastEvent() {
        registerCommonVm(vm)
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


}