package com.biiiiit.kokonats.ui.custome

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.biiiiit.kokonats.databinding.ViewEnergyShowBinding
import com.biiiiit.lib_base.user.loginOrAction
import com.biiiiit.lib_base.utils.dp2px
import com.biiiiit.lib_base.utils.jump2Main

/**
 * @Author yo_hack
 * @Date 2021.10.31
 * @Description energy show add view
 **/
class EnergyShowView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewEnergyShowBinding =
        ViewEnergyShowBinding.inflate(LayoutInflater.from(context), this)


    init {
        val dp4 = 4.dp2px(context)
        setPadding(dp4, dp4, dp4 / 2, dp4)
        binding.root.setOnClickListener(OnClickListener {
            loginOrAction {
                jump2Main(context, 0)
            }
        })
    }


    fun setNewEnergyLong(energy: Long) {
        binding.tvEnergy.text = energy.toString()
    }
}