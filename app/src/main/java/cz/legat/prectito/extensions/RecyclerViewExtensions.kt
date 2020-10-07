package cz.legat.prectito.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.prectito.ui.main.authors.AuthorsAdapter
import cz.legat.prectito.ui.main.base.BaseAdapter

fun <E> RecyclerView.initLinear(dataAdapter: BaseAdapter<E>?) {
    setHasFixedSize(true)
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    adapter = dataAdapter
}

fun RecyclerView.initLinearPaged(dataAdapter: AuthorsAdapter?) {
    setHasFixedSize(true)
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    adapter = dataAdapter
}