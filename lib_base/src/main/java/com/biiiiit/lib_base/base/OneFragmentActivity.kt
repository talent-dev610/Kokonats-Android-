package com.biiiiit.lib_base.base

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.biiiiit.lib_base.R
import com.biiiiit.lib_base.databinding.ActivityOneFragmentBinding

/**
 * @Author yo_hack
 * @Date 2021.11.01
 * @Description one fragment activity
 **/
abstract class OneFragmentActivity : BaseActivity<ActivityOneFragmentBinding, EmptyViewModel>() {

    @Suppress("MemberVisibilityCanBePrivate", "PropertyName")
    protected val TAG_ONE_FRAGMENT = "tag_one_fragment"

    override fun getVMClazz(): Class<EmptyViewModel> = EmptyViewModel::class.java

    override fun createBinding(layoutInflater: LayoutInflater): ActivityOneFragmentBinding =
        ActivityOneFragmentBinding.inflate(layoutInflater)


    override fun initView1() {
        var fragment = supportFragmentManager.findFragmentByTag(TAG_ONE_FRAGMENT)
        if (fragment == null) {
            fragment = createFragment()
        }
        supportFragmentManager.commit(true) {
            add(R.id.fl_root, fragment, TAG_ONE_FRAGMENT)
        }
    }

    abstract fun createFragment(): Fragment
}