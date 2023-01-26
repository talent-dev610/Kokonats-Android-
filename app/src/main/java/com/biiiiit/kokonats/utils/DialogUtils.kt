package com.biiiiit.kokonats.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.databinding.DialogCommonBinding
import com.biiiiit.lib_base.utils.getScreenWidth

/**
 * @Author yo_hack
 * @Date 2022.01.14
 * @Description
 **/


class ShowCommonData(
    val imgRes: Int,
    val titleRes: Int,
    val descRes: Int,
    val leftRes: Int,
    val rightRes: Int,
    val wPercent: Float = 0.8f
)

class ShowCommonDataRes(
    val imgRes: Int,
    val titleRes: Int,
    val descRes: String,
    val leftRes: Int,
    val rightRes: Int,
    val wPercent: Float = 0.8f
)

fun showCommonDialog(context: Context?, data: ShowCommonData, left: (() -> Unit)? = null, right: (() -> Unit)? = null): Dialog? {
    if (context == null) {
        return null
    }


    val dialog = Dialog(context, R.style.common_dialog)
    val rootBinding = DialogCommonBinding.inflate(LayoutInflater.from(context))
    dialog.setContentView(rootBinding.root)
    dialog.setCancelable(true)
    dialog.setCanceledOnTouchOutside(true)

    dialog.window?.let { window ->
        val lp = window.attributes
        lp.width = (getScreenWidth(context) * data.wPercent).toInt()
    }
    dialog.show()

    rootBinding.ivIcon.setImageResource(data.imgRes)


    rootBinding.tvTitle.isVisible = (data.titleRes != 0).apply {
        if (this) {
            rootBinding.tvTitle.setText(data.titleRes)
        }
    }
    rootBinding.tvDesc.isVisible = (data.descRes != 0).apply {
        if (this) {
            rootBinding.tvDesc.setText(data.descRes)
        }
    }


    rootBinding.tvLeft.setText(data.leftRes)
    rootBinding.tvRight.setText(data.rightRes)

    rootBinding.tvLeft.setOnClickListener {
        dialog.dismiss()
        left?.invoke()
    }
    rootBinding.tvRight.setOnClickListener {
        dialog.dismiss()
        right?.invoke()
    }

    return dialog
}

fun showCommonDialogRes(context: Context?, data: ShowCommonDataRes, left: (() -> Unit)? = null, right: (() -> Unit)? = null): Dialog? {
    if (context == null) {
        return null
    }


    val dialog = Dialog(context, R.style.common_dialog)
    val rootBinding = DialogCommonBinding.inflate(LayoutInflater.from(context))
    dialog.setContentView(rootBinding.root)
    dialog.setCancelable(true)
    dialog.setCanceledOnTouchOutside(true)

    dialog.window?.let { window ->
        val lp = window.attributes
        lp.width = (getScreenWidth(context) * data.wPercent).toInt()
    }
    dialog.show()

    rootBinding.ivIcon.setImageResource(data.imgRes)


    rootBinding.tvTitle.isVisible = (data.titleRes != 0).apply {
        if (this) {
            rootBinding.tvTitle.setText(data.titleRes)
        }
    }
    rootBinding.tvDesc.isVisible = (data.descRes.isNotEmpty()).apply {
        if (this) {
            rootBinding.tvDesc.text = data.descRes
        }
    }


    rootBinding.tvLeft.setText(data.leftRes)
    rootBinding.tvRight.setText(data.rightRes)

    rootBinding.tvLeft.setOnClickListener {
        dialog.dismiss()
        left?.invoke()
    }
    rootBinding.tvRight.setOnClickListener {
        dialog.dismiss()
        right?.invoke()
    }

    return dialog
}