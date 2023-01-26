package com.biiiiit.lib_base.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics

/**
 * @Author yo_hack
 * @Date 2021.10.13
 * @Description dimen utils
 **/

fun Int.dp2px(context: Context?): Int = dp2px(context, this)
fun Int.sp2px(context: Context?): Int = sp2px(context, this)
fun Int.px2dp(context: Context?): Int = px2dp(context, this)

fun Float.dp2px(context: Context?): Float = dp2px(context, this)
fun Float.sp2px(context: Context?): Float = sp2px(context, this)
fun Float.px2dp(context: Context?): Float = px2dp(context, this)


/**
 * dp to px
 * @param dp int dp
 */
fun dp2px(context: Context?, dp: Int): Int =
  (dp * getDisplayMetrics(context).density + 0.5f).toInt()

/**
 * dp to px
 * @param dp float dp
 */
fun dp2px(context: Context?, dp: Float): Float = dp * getDisplayMetrics(context).density

/**
 * sp to px
 * @param sp int sp
 */
fun sp2px(context: Context?, sp: Int): Int =
  (sp * getDisplayMetrics(context).scaledDensity + 0.5f).toInt()

/**
 * sp to px
 * @param sp float sp
 */
fun sp2px(context: Context?, sp: Float): Float = sp * getDisplayMetrics(context).scaledDensity

/**
 * px to dp
 * @param px int px
 */
fun px2dp(context: Context?, px: Int): Int =
  (px / getDisplayMetrics(context).density + 0.5f).toInt()

/**
 * px to dp
 * @param px float px
 */
fun px2dp(context: Context?, px: Float): Float = px / getDisplayMetrics(context).density + 0.5f

fun getDisplayMetrics(context: Context?): DisplayMetrics =
  (context?.resources ?: Resources.getSystem()).displayMetrics

/**
 * get screen width
 */
fun getScreenWidth(context: Context?) = getDisplayMetrics(context).widthPixels

/**
 * get screen height
 */
fun getScreenHeight(context: Context?) = getDisplayMetrics(context).heightPixels