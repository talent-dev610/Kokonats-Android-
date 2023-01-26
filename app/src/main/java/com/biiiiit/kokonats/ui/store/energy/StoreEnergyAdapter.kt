package com.biiiiit.kokonats.ui.store.energy

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.NetInAppProduct
import com.biiiiit.lib_base.adapter.BaseGridAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @Author Lucas Jordan
 * @Date 2022.04.13
 * @Description
 */

class StoreEnergyAdapter internal constructor(private val context: Context): BaseGridAdapter<NetInAppProduct> (context, R.layout.item_home_store_energy) {
    override fun convert(rootView: View, item: NetInAppProduct) {
        if (item.productId == "0") {
            rootView.findViewById<TextView>(R.id.tv_recommend).visibility = View.VISIBLE
            rootView.findViewById<TextView>(R.id.tv_price).setTextColor(ContextCompat.getColor(context, R.color.red_eb5757))
        } else {
            rootView.findViewById<TextView>(R.id.tv_recommend).visibility = View.GONE
            rootView.findViewById<TextView>(R.id.tv_price).setTextColor(ContextCompat.getColor(context, R.color.color_white))
        }
        rootView.findViewById<TextView>(R.id.tv_energy).setText("+${item.energy}")
        val price = if (item.localPrice.isNullOrBlank()) {
            (item.jpyPrice / 100.0).toString()
        } else {
            item.localPrice
        }
        rootView.findViewById<TextView>(R.id.tv_price).setText("¥$price")
    }
}