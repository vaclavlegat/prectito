package cz.legat.core.extensions

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.core.base.BaseAdapter

fun <E> RecyclerView.simpleGrid(dataAdapter: BaseAdapter<E>?) {
    setHasFixedSize(true)
    layoutManager = GridLayoutManager(context, 3)
    adapter = dataAdapter
}

fun <E> RecyclerView.simpleLinear(dataAdapter: BaseAdapter<E>?) {
    setHasFixedSize(true)
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    adapter = dataAdapter
}