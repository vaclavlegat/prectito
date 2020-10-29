package cz.legat.core.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.core.base.BaseAdapter

fun <E> RecyclerView.initLinear(dataAdapter: BaseAdapter<E>?) {
    setHasFixedSize(true)
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    adapter = dataAdapter
}