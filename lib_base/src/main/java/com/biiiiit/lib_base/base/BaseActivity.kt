package com.biiiiit.lib_base.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.biiiiit.lib_base.BaseApp
import com.biiiiit.lib_base.utils.dialog.showLoadingDialog
import com.biiiiit.lib_base.utils.showMsg
import com.imuxuan.floatingview.FloatingView


/**
 * @Author yo_hack
 * @Date 2021.10.15
 * @Description base activity for this app
 **/
abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel>() : AppCompatActivity(),
    ILoadAndToastEvent {


    /** binding **/
    protected lateinit var binding: VB

    /** vm **/
    protected val vm: VM by lazy {
        ViewModelProvider(this).get(getVMClazz())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeOnCreate0()
        super.onCreate(savedInstanceState)
        binding = createBinding(layoutInflater)
        setContentView(binding.root)
        initView1()
        registerLoadAndToastEvent()
        initViewModel2()
        actionOnce()
    }


    /**
     * get vm clazz
     */
    abstract fun getVMClazz(): Class<VM>

    /**
     *  create binding
     */
    abstract fun createBinding(layoutInflater: LayoutInflater): VB

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
     * method for query network or something
     */
    protected open fun actionOnce() = Unit

    /**
     * method for query network or something
     */
    protected open fun actionAlways() = Unit

    /**
     * register common viewModel
     * use for multi register
     */
    protected open fun registerLoadAndToastEvent() {
        registerCommonVm(vm)
    }

    override fun getLOwner(): LifecycleOwner = this

    private var dialog: Dialog? = null
    override fun showLoading(msg: String?) {
        if (dialog == null) {
            dialog = showLoadingDialog(this, msg)
        } else {
            if (dialog?.isShowing != true) {
                dialog?.show()
            }
        }
    }

    override fun dismissLoading() {
        dialog?.dismiss()
        dialog = null
    }

    override fun showToast(msg: String?) {
        showMsg(msg)
    }

    override fun onResume() {
        super.onResume()
        actionAlways()
    }

    protected open val showFloat: Boolean = true

    override fun onDestroy() {
        dismissLoading()
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        if (showFloat) {
            FloatingView.get().attach(this)
        }
    }

    override fun onStop() {
        if (showFloat) {
            FloatingView.get().detach(this)
        }
        super.onStop()
    }

    fun <AV : ViewModel> getAppViewModel(clazz: Class<AV>): AV {
        return (application as BaseApp).getAppViewModelProvider().get(clazz)
    }
}