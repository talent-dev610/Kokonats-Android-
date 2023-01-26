package com.biiiiit.kokonats.ui.custome

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.biiiiit.kokonats.databinding.ViewSponsorType0Binding
import com.biiiiit.lib_base.utils.photo.loadCropRound

/**
 * @Author yo_hack
 * @Date 2021.11.01
 * @Description
 **/
class SponsorType0View @JvmOverloads
constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewSponsorType0Binding.inflate(LayoutInflater.from(context), this)

    fun setSposorData() {
        binding.ivSponsorLogo.loadCropRound(
            "https://www.riotgames.com/darkroom/564/4d105e24b06ba594da0708fe8ded1314:34aa2668b5471ad623692ea3f72541dd/lol-wildrift-productimage.png", 8, 0, 0
        )
    }

}