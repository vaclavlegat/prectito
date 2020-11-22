package cz.legat.core.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.jsoup.Connection

const val TITLE = 0
const val ITEM = 1

abstract class BaseAdapter<T>(private val onItemClickedListener: OnItemClickedListener<T>) :
    RecyclerView.Adapter<BaseAdapter<T>.ParentViewHolder>() {

    interface OnItemClickedListener<T> {
        fun onItem(item: T)
    }

    private val items = mutableListOf<Item<T>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(if (viewType == TITLE) titleLayout()!! else layout(), parent, false) as View
        return if (viewType == TITLE) titleViewHolder(view)!! else  viewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && !title().isNullOrEmpty()) {
            TITLE
        } else {
            ITEM
        }
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val item = items[position]
        when(holder){
            is TitleBaseViewHolder -> holder.bind(item.title!!)
            is BaseViewHolder -> {
                holder.bind(item.item!!, position)
                holder.pview.setOnClickListener { onItemClickedListener.onItem(item.item) }
            }
        }
    }

    fun update(updatedItems: List<T>) {
        items.clear()
        items.addAll(updatedItems.map {
            Item(null, it)
        })
        if(!title().isNullOrEmpty()){
            items.add(0, Item(title(),null))
        }
        notifyDataSetChanged()
    }

    abstract fun layout(): Int
    open fun titleLayout(): Int? = null
    open fun title(): String? = null
    abstract fun viewHolder(view: View): BaseViewHolder
    open fun titleViewHolder(view: View): TitleBaseViewHolder? = null

    abstract inner class ParentViewHolder(val pview: View) : RecyclerView.ViewHolder(pview){
    }

    abstract inner class BaseViewHolder(val view: View) : ParentViewHolder(view) {
        abstract fun bind(item: T, position: Int)
    }

    abstract inner class TitleBaseViewHolder(val view: View) : ParentViewHolder(view) {
        abstract fun bind(item: String)
    }

    data class Item<T> (val title:String? = null, val item:T? = null)
}
