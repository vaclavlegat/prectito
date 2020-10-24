package cz.legat.core.extensions

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.core.base.BaseAdapter

fun <E> RecyclerView.initLinear(dataAdapter: BaseAdapter<E>?) {
    setHasFixedSize(true)
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    adapter = dataAdapter
}

fun <T, E: RecyclerView.ViewHolder> RecyclerView.initLinearPaged(dataAdapter: PagedListAdapter<T, E>) {
    setHasFixedSize(true)
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    adapter = dataAdapter
}