package com.biiiiit.lib_base.base

import android.app.Dialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.biiiiit.lib_base.BaseApp
import com.biiiiit.lib_base.utils.dialog.showLoadingDialog
import com.biiiiit.lib_base.utils.showMsg

/**
 * @Author yo_hack
 * @Date 2021.11.25
 * @Description
 **/
open class AbsFragment : Fragment(), ILoadAndToastEvent {

    override fun getLOwner(): LifecycleOwner = viewLifecycleOwner

    private var dialog: Dialog? = null
    override fun showLoading(msg: String?) {
        if (dialog == null) {
            dialog = showLoadingDialog(context, msg)
        } else {
            if (dialog?.isShowing != true) {
                dialog?.show()
            }
        }
    }

    override fun dismissLoading() {
        dialog?.dismiss()
    }

    override fun showToast(msg: String?) {
        showMsg(msg)
    }

    override fun onDestroyView() {
        dismissLoading()
        super.onDestroyView()
    }

    fun <AV : ViewModel> getAppViewModel(clazz: Class<AV>): AV {
        return BaseApp.app.getAppViewModelProvider().get(clazz)
    }
}