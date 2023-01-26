package com.biiiiit.kokonats.ui.store.item

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.BaseAdapter
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.biiiiit.kokonats.data.bean.Game
import com.biiiiit.kokonats.data.bean.PurchasedGameItem
import com.biiiiit.kokonats.databinding.ActivityStoreGameBinding
import com.biiiiit.kokonats.ui.game.viewmodel.GameDetailViewModel
import com.biiiiit.kokonats.ui.user.vm.UserKokoViewModel
import com.biiiiit.lib_base.adapter.OnGridItemClickListener
import com.biiiiit.lib_base.base.BaseActivity
import com.biiiiit.lib_base.data.COMMON_DATA
import com.biiiiit.lib_base.utils.ROUTE_STORE_ENERGY
import com.biiiiit.lib_base.utils.ROUTE_STORE_GAME
import com.biiiiit.lib_base.utils.photo.loadCropRound
import java.lang.Exception

/**
 * @Author Lucas Jordan
 * @Date 2022.04.13
 * @Description game items store
 */

@Route(path = ROUTE_STORE_GAME)
class StoreGameActivity : BaseActivity<ActivityStoreGameBinding, StoreGameViewModel>() {

    private val kokoVM: UserKokoViewModel by lazy {
        getAppViewModel(UserKokoViewModel::class.java)
    }

    private val gameVM: GameDetailViewModel by lazy {
        getAppViewModel(GameDetailViewModel::class.java)
    }

    private lateinit var game: Game

    private lateinit var itemAdapter: StoreItemAdapter

    private lateinit var purchasedItems: MutableList<PurchasedGameItem>

    override fun initView1() {
        super.initView1()
        binding.ivBack.setOnClickListener(View.OnClickListener { finish() })
        binding.tvTitle.text = game.name
        binding.ivCover.loadCropRound(game.iconUrl, 0, 0, 0)
        itemAdapter = StoreItemAdapter(baseContext)
        itemAdapter.setOnItemClickListener(object : OnGridItemClickListener {
            override fun onItemClick(adapter: BaseAdapter, view: View, position: Int) {
                val purchasedItem : PurchasedGameItem? = try {
                    purchasedItems.first {pItem ->  pItem.gameItemId == itemAdapter.getItem(position).id}
                } catch (e: Exception) {
                    null;
                }
                if (purchasedItem != null) return;
                vm.purchaseGameItem(itemAdapter.getItem(position).id)
            }
        })
        binding.gvItems.adapter = itemAdapter
    }

    override fun initViewModel2() {
        super.initViewModel2()
        kokoVM.koko.observe(getLOwner()) {
            binding.tvCredit.text = "%,d".format(it.toInt())
        }
        gameVM.gameItems.observe(getLOwner()) {
            itemAdapter.setNewInstance(it)
            if (it.isEmpty()) {
                binding.tvComingSoon.visibility = View.VISIBLE
                binding.gvItems.visibility = View.GONE
            } else {
                binding.tvComingSoon.visibility = View.GONE
                binding.gvItems.visibility = View.VISIBLE
            }
        }
        vm.gameItemsPurchased.observe(getLOwner()) {
            purchasedItems = it
            itemAdapter.setPurchasedItems(it)
        }
    }

    override fun beforeOnCreate0() {
        super.beforeOnCreate0()
        game = intent.getSerializableExtra(COMMON_DATA) as Game
    }

    override fun actionAlways() {
        super.actionAlways()
        kokoVM.requestKoko()
        gameVM.queryGameItems(game.id)
        vm.queryPurchasedGameItems()
    }

    override fun getVMClazz(): Class<StoreGameViewModel> = StoreGameViewModel::class.java

    override fun createBinding(layoutInflater: LayoutInflater): ActivityStoreGameBinding = ActivityStoreGameBinding.inflate(layoutInflater)
}