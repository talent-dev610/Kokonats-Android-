package com.biiiiit.kokonats.utils

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan

/**
 * @Author yo_hack
 * @Date 2022.01.11
 * @Description txt utils
 **/
class ColorTxtVo(
    //更改的字体
    var txt: String? = null,
    //更改的颜色
    var color: Int? = Color.parseColor("#E55435"),
    //更改的大小
    var ratio: Float = 1.0f
)

fun colorSizeTxt(
    dest: String?,
    vararg list: ColorTxtVo
): SpannableString? {
    dest?.let {
        val sb = SpannableString(dest)

        list.forEach {
            it.txt?.let { txt ->
                val begin = dest.indexOf(txt)
                if (begin > -1) {
                    val end = begin + txt.length

                    it.color?.let { color ->
                        val colorSpan = ForegroundColorSpan(color)
                        sb.setSpan(colorSpan, begin, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                    }

                    if (it.ratio > 0) {
                        val rs = RelativeSizeSpan(it.ratio)
                        sb.setSpan(rs, begin, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                    }
                }
            }
        }
        return sb
    }
    return null
}

fun colorSizeTxt(dest: String?, list: List<ColorTxtVo>): SpannableString? =
    colorSizeTxt(dest, *list.toTypedArray())