package com.biiiiit.lib_base.utils

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * @Author yo_hack
 * @Date 2021.10.15
 * @Description toast
 **/
fun Context.showMsg(msg: String?, length: Int = Toast.LENGTH_SHORT) {
    if (msg.isNullOrEmpty().not()) {
        val normal = Toast.makeText(this, msg, length)
//    normal.setGravity(Gravity.CENTER, 0, 0)
        normal.show()
    }
}

fun Fragment.showMsg(msg: String?, length: Int = Toast.LENGTH_SHORT) {
    if (context != null && msg.isNullOrEmpty().not()) {
        val normal = Toast.makeText(context, msg, length)
//    normal.setGravity(Gravity.CENTER, 0, 0)
        normal.show()
    }
}

fun Activity.showMsg(msg: String?, length: Int = Toast.LENGTH_SHORT) {
    if (msg.isNullOrEmpty().not()) {
        val normal = Toast.makeText(this, msg, length)
//    normal.setGravity(Gravity.CENTER, 0, 0)
        normal.show()
    }
}

