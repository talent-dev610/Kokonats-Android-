package com.biiiiit.kokonats.ui.game.ui.adapter

import android.graphics.Rect
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.TnmtPayout
import com.biiiiit.kokonats.data.bean.TournamentClass
import com.biiiiit.kokonats.data.repo.PLAY_TYPE_PVP
import com.biiiiit.lib_base.BaseApp
import com.biiiiit.lib_base.utils.dp2px
import com.biiiiit.lib_base.utils.fromJson
import com.biiiiit.lib_base.utils.getScreenWidth
import com.biiiiit.lib_base.utils.photo.loadCropRound
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.gson.Gson

/**
 * @Author yo_hack
 * @Date 2021.10.31
 * @Description 首页 gameTab  -> tnmt
 **/
class HomeGameTnmtAdapter :
    BaseQuickAdapter<TournamentClass, BaseViewHolder>(R.layout.item_home_game_tab_tnmt) {

    private var singleWidth =
        getScreenWidth(null) - BaseApp.app.resources.getDimensionPixelSize(R.dimen.common_margin_53) -
            3.dp2px(null) - 47.dp2px(null)


    /**
     * person pool
     */
    private val personPool: RecyclerView.RecycledViewPool by lazy {
        RecyclerView.RecycledViewPool()
    }

    /**
     * key pool
     */
    private val keyPool: RecyclerView.RecycledViewPool by lazy {
        RecyclerView.RecycledViewPool()
    }

    private val decor: RecyclerView.ItemDecoration by lazy {
        object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.left = -context.resources.getDimensionPixelOffset(R.dimen.min_avatar_30) / 3
                }
            }
        }
    }

    var requestPlayersCall: ((tnmtCId: Long) -> Unit)? = null

    init {
        setDiffCallback(object : DiffUtil.ItemCallback<TournamentClass>() {
            override fun areItemsTheSame(
                oldItem: TournamentClass, newItem: TournamentClass
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: TournamentClass, newItem: TournamentClass
            ): Boolean {
                return oldItem.description == newItem.description &&
                    oldItem.tournamentName == newItem.tournamentName &&
                    oldItem.thumbnail == newItem.thumbnail &&
                    oldItem.entryFee == newItem.entryFee &&
                    oldItem.keyword == newItem.keyword &&
                    oldItem.tag == newItem.tag &&
                    oldItem.players == newItem.players &&
                    oldItem.players?.size == newItem.players?.size
            }

        })
    }

    override fun convert(holder: BaseViewHolder, item: TournamentClass) {
        holder.itemView.layoutParams = holder.itemView.layoutParams.apply {
            this.width = singleWidth
        }

        val win = if (item.type == PLAY_TYPE_PVP) {
            item.winningPayout
        } else {
            val ranks: MutableList<TnmtPayout>? = Gson().fromJson(item.rankingPayout)
            ranks?.maxOf { it.payout } ?: 0
        }

        holder.setText(R.id.tv_title, item.tournamentName)
            .setText(R.id.tv_desc, item.description)
            .setText(R.id.tv_koko, win.toString())
            .setText(R.id.tv_cost, context.getString(R.string.game_need_energy, item.entryFee))


        holder.getView<ImageView>(R.id.iv_cover).loadCropRound(item.thumbnail, 9, 0, 0)


        // person
        val rcvPerson = holder.getView<RecyclerView>(R.id.rcv_person)
        val isNotPvp = item.type != PLAY_TYPE_PVP
        rcvPerson.isVisible = isNotPvp
        rcvPerson.setRecycledViewPool(personPool)
        rcvPerson.removeItemDecoration(decor)
        rcvPerson.addItemDecoration(decor)
        rcvPerson.layoutManager =
            rcvPerson.layoutManager ?: LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rcvPerson.adapter =
            ((rcvPerson.adapter as? HomeGameTnmtPersonAdapter) ?: HomeGameTnmtPersonAdapter())
                .apply {
                    when {
                        item.players == null -> {
                            if (isNotPvp) {
                                requestPlayersCall?.invoke(item.id)
                            } else {
                                rcvPerson.isVisible = false
                            }
                            setNewInstance(mutableListOf())
                        }
                        item.players!!.isEmpty() -> {
                            setNewInstance(mutableListOf())
                            rcvPerson.isVisible = false
                        }
                        else -> {
                            val result = mutableListOf<MultiTnmtPersonEntity>()
                            val players = item.players!!
                            result.addAll(players.subList(0, minOf(4, players.size)).map { MultiTnmtPersonEntity(it) })
                            result.add(MultiTnmtPersonEntity(Pair(players.size, item.participantNumber)))
                            setNewInstance(result)
                        }
                    }
                }


        val rcvKey = holder.getView<RecyclerView>(R.id.rcv_key)
        rcvKey.setRecycledViewPool(keyPool)
        val words = item.colorList

        val isEmpty = words.isNullOrEmpty()

        rcvKey.isVisible = !isEmpty
        if (!isEmpty) {
            rcvKey.layoutManager = rcvKey.layoutManager ?: FlexboxLayoutManager(context)
            rcvKey.adapter =
                ((rcvPerson.adapter as? HomeGameTnmtKeyAdapter) ?: HomeGameTnmtKeyAdapter())
                    .apply {
                        setNewInstance(words)
                    }
        }

    }
}