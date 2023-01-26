package com.biiiiit.lib_base

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.biiiiit.lib_base.utils.ActivityManger
import com.biiiiit.lib_base.utils.logi
import com.facebook.stetho.Stetho
import com.facebook.stetho.rhino.JsRuntimeReplFactoryBuilder
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel

/**
 * @Author yo_hack
 * @Date 2021.10.13
 * @Description base app 基类
 **/
open class BaseApp : MultiDexApplication(), ViewModelStoreOwner {

    private lateinit var mAppViewModelStore: ViewModelStore

    private var mFactory: ViewModelProvider.Factory? = null

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }

    companion object {
        lateinit var app: BaseApp
    }


    override fun onCreate() {
        super.onCreate()
        app = this
        mAppViewModelStore = ViewModelStore()

        registerAMCallBack()
        initArouter()
        initMMKV()
        initDebug()
    }

    /**
     * init mmkv
     */
    private fun initMMKV() {
        MMKV.initialize(
            this, if (BuildConfig.DEBUG) {
                MMKVLogLevel.LevelDebug
            } else {
                MMKVLogLevel.LevelError
            }
        )
    }

    private fun initArouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)
    }

    private fun initDebug() {
        if (BuildConfig.DEBUG) {
//            Stetho.initialize(Stetho.newInitializerBuilder(this)
//                .enableWebKitInspector {
//                    Stetho.DefaultInspectorModulesBuilder(this)
//                        .runtimeRepl(
//                            JsRuntimeReplFactoryBuilder(this)
//                                .build()
//                        )
//                        .finish()
//                }
//                .build())
            Stetho.initializeWithDefaults(this)
        }
    }


    /**
     * register activity manager Call back for logs
     */
    private fun registerAMCallBack() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                ActivityManger.pushActivity(activity)
                "OnActivityCreated ${activity.localClassName}".logi("am")
            }

            override fun onActivityStarted(activity: Activity) {
                "onActivityStarted ${activity.localClassName}".logi("am")
            }

            override fun onActivityResumed(activity: Activity) {
                "onActivityResumed ${activity.localClassName}".logi("am")
            }

            override fun onActivityPaused(activity: Activity) {
                "onActivityPaused ${activity.localClassName}".logi("am")
            }

            override fun onActivityStopped(activity: Activity) {
                "onActivityStopped ${activity.localClassName}".logi("am")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                "onActivitySaveInstanceState ${activity.localClassName}".logi("am")
            }

            override fun onActivityDestroyed(activity: Activity) {
                ActivityManger.popActivity(activity)
                "onActivityDestroyed ${activity.localClassName}".logi("am")
            }

        })
    }



    /**
     * 获取一个全局的ViewModel
     */
    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }

}