package com.biiiiit.lib_base.adapter

import android.view.View
import android.widget.BaseAdapter

interface OnGridItemClickListener {
    fun onItemClick(adapter : BaseAdapter, view: View, position: Int)
}