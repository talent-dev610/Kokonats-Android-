package com.biiiiit.lib_base.utils

import android.app.Activity
import java.util.*

/**
 * @Author yo_hack
 * @Date 2021.10.13
 * @Description instance of activity manager
 **/
object ActivityManger {
    private val mActivityList = LinkedList<Activity>()
    val currentActivity: Activity?
        get() =
            if (mActivityList.isEmpty()) null
            else mActivityList.last

    /**
     * add activity
     */
    fun pushActivity(activity: Activity) {
        if (mActivityList.contains(activity)) {
            if (mActivityList.last != activity) {
                mActivityList.remove(activity)
                mActivityList.add(activity)
            }
        } else {
            mActivityList.add(activity)
        }
    }

    /**
     * remove activity
     */
    fun popActivity(activity: Activity) {
        mActivityList.remove(activity)
    }

    /**
     * close current activity
     */
    fun finishCurrentActivity() {
        currentActivity?.finish()
    }

    /**
     * close activity
     */
    fun finishActivity(activity: Activity) {
        mActivityList.remove(activity)
        activity.finish()
    }

    /**
     * close all activity with class name
     */
    fun finishActivity(clazz: Class<*>) {
        for (activity in mActivityList)
            if (activity.javaClass == clazz)
                activity.finish()
    }

    /**
     * close all activity
     */
    fun finishAllActivity() {
        for (activity in mActivityList)
            activity.finish()
    }

    /**
     * contains activity or not?
     */
    fun containActivity(clazz: Class<*>) =
        mActivityList.find {
            it.javaClass == clazz
        } != null


    fun listSize() = mActivityList.size
}