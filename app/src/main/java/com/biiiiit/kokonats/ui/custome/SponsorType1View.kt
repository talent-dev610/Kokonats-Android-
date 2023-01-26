package com.biiiiit.kokonats.ui.custome

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.databinding.ViewSponsorType1Binding
import com.biiiiit.lib_base.utils.copyText
import com.biiiiit.lib_base.utils.jump2Outside
import com.biiiiit.lib_base.utils.photo.loadCropRound
import com.biiiiit.lib_base.utils.photo.loadRadius
import com.biiiiit.lib_base.utils.showMsg

/**
 * @Author yo_hack
 * @Date 2021.11.01
 * @Description
 **/
class SponsorType1View @JvmOverloads
constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewSponsorType1Binding.inflate(LayoutInflater.from(context), this)

    private var toastResId = 0

    init {
        this.setBackgroundResource(R.drawable.rect9_black21283f)

        binding.ivCopy.setOnClickListener {
            val str = binding.tvUrl.text.toString()
            val toastStr = context.getString(
                if (!str.isNullOrBlank() && copyText(context, str)) {
                    toastResId
                } else {
                    R.string.copy_fail
                }
            )
            context.showMsg(toastStr)
        }

        setOnClickListener {
            val str = binding.tvUrl.text.toString()
            if (!str.isNullOrBlank()) {
                jump2Outside(context, str)
            }
        }

    }

    fun setSponsorData(
        cover: Any,
        h5Url: String?,
        title: String? = null,
        toastId: Int = R.string.copy_success,
    ) {
        binding.ivSponsorLogo.loadRadius(cover, 8, 0, 0)
        binding.tvHint.text = title
        binding.tvHint.isVisible = !title.isNullOrBlank()
        binding.tvUrl.text = h5Url
        toastResId = toastId
    }


}