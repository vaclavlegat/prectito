package cz.legat.core.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>(private val onItemClickedListener: OnItemClickedListener<T>) :
    RecyclerView.Adapter<BaseAdapter<T>.BaseViewHolder>() {

    interface OnItemClickedListener<T> {
        fun onItem(item: T)
    }

    private val items = mutableListOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layout(), parent, false) as View
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.view.setOnClickListener { onItemClickedListener.onItem(item) }
    }

    fun update(updatedItems: List<T>) {
        items.clear()
        items.addAll(updatedItems)
        notifyDataSetChanged()
    }

    abstract fun layout(): Int
    abstract fun viewHolder(view: View): BaseViewHolder

    abstract inner class BaseViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: T)
    }
}