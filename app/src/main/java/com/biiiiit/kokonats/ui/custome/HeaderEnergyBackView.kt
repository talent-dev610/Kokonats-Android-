package com.biiiiit.kokonats.ui.custome

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.biiiiit.kokonats.databinding.ViewHeaderEnergyBackBinding

/**
 * @Author yo_hack
 * @Date 2021.10.31
 * @Description energy back view
 **/
class HeaderEnergyBackView @JvmOverloads
constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewHeaderEnergyBackBinding =
        ViewHeaderEnergyBackBinding.inflate(LayoutInflater.from(context), this)


    fun setNewEnergyLong(energy: Long) {
        binding.energyView.setNewEnergyLong(energy)
        if (energy < 0) {
            binding.energyView.visibility = View.INVISIBLE
        }
    }

    fun setBackClick(cb: () -> Unit) {
        binding.ivBack.setOnClickListener {
            cb.invoke()
        }
    }
}