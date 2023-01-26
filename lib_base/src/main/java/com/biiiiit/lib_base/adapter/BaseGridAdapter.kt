package com.biiiiit.lib_base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes

/**
 * @Author Lucas Jordan
 * @Date 2022.04.13
 * @Description base gridview adapter
 */
abstract class BaseGridAdapter<T>
@JvmOverloads constructor(
    private val context: Context,
    @LayoutRes private val layoutResId: Int,
    data: MutableList<T>? = null
) : BaseAdapter() {
    var data: MutableList<T> = data ?: arrayListOf()
        internal set
    private var mOnItemClickListener: OnGridItemClickListener? = null

    open fun setNewInstance(list: MutableList<T>?) {
        if (list === this.data) {
            return
        }
        this.data = list ?: arrayListOf()
        notifyDataSetChanged()
    }

    open fun setData(@IntRange(from = 0) index: Int, data: T) {
        if (index >= this.data.size) {
            return
        }
        this.data[index] = data
        notifyDataSetChanged()
    }

    open fun addData(@IntRange(from = 0) position: Int, data: T) {
        this.data.add(position, data)
        notifyDataSetChanged()
        compatibilityDataSizeChanged(1)
    }

    protected fun compatibilityDataSizeChanged(size: Int) {
        if (this.data.size == size) {
            notifyDataSetChanged()
        }
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(p0: Int): T {
        return data[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    protected abstract fun convert(rootView: View, item: T)

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view: View = p1 ?: View.inflate(context, layoutResId, null)
        view.setOnClickListener(View.OnClickListener {
            mOnItemClickListener!!.onItemClick(this, view, p0)
        })
        convert(view, getItem(p0))
        return view
    }

    fun setOnItemClickListener(listener: OnGridItemClickListener?) {
        this.mOnItemClickListener = listener
    }

    fun getOnItemClickListener(): OnGridItemClickListener? = mOnItemClickListener
}