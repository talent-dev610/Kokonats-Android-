package com.biiiiit.kokonats.ui.store.item

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.GameItem
import com.biiiiit.kokonats.data.bean.PurchasedGameItem
import com.biiiiit.lib_base.adapter.BaseGridAdapter
import com.biiiiit.lib_base.utils.photo.loadInsideRound
import java.lang.Exception

/**
 * @Author Lucas Jordan
 * @Date 2022.04.13
 */
class StoreItemAdapter internal constructor(private val context: Context, purchasedGameItems: MutableList<PurchasedGameItem>? = null): BaseGridAdapter<GameItem>(context, R.layout.item_store_game_item) {
    var purchased: MutableList<PurchasedGameItem> = purchasedGameItems ?: arrayListOf()

    fun setPurchasedItems(list: MutableList<PurchasedGameItem>?) {
        if (list === this.purchased) return
        this.purchased = list ?: arrayListOf()
        notifyDataSetChanged()
    }

    override fun convert(rootView: View, item: GameItem) {
        rootView.findViewById<ImageView>(R.id.iv_cover).loadInsideRound(item.pictureUrl, 1, 0, 0)
        val purchasedItem : PurchasedGameItem? = try {
            purchased.first {pItem ->  pItem.gameItemId == item.id}
        } catch (e: Exception) {
            null;
        }

        if (purchasedItem == null) {
            rootView.findViewById<View>(R.id.view_mask).visibility = View.GONE
            rootView.findViewById<ImageView>(R.id.iv_credit).visibility = View.VISIBLE
            rootView.findViewById<TextView>(R.id.tv_price).setTextColor(context.getColor(R.color.yellow_ffcb00))
            rootView.findViewById<TextView>(R.id.tv_price).setText("%,d".format(item.kokoPrice))
        } else {
            rootView.findViewById<View>(R.id.view_mask).visibility = View.VISIBLE
            rootView.findViewById<ImageView>(R.id.iv_credit).visibility = View.GONE
            rootView.findViewById<TextView>(R.id.tv_price).setTextColor(context.getColor(R.color.color_white))
            rootView.findViewById<TextView>(R.id.tv_price).setText(context.getText(R.string.purchased))
        }
    }
}