package cz.legat.books.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.legat.books.R
import cz.legat.core.extensions.fadeInText
import cz.legat.core.extensions.loadImg
import cz.legat.core.model.Book
import cz.legat.core.model.bigImgLink
import cz.legat.core.model.middleImgLink

class BooksAdapter(private val onItemClickedListener: OnItemClickedListener<Book>) :
    RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    interface OnItemClickedListener<T> {
        fun onItem(item: T)
    }

    private val items = mutableListOf<Book>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pt_item_book, parent, false) as View

        val viewWidth: Int = (parent.measuredWidth / 3)
        val height: Int = (viewWidth * 1.6).toInt()
        val viewParams = view.layoutParams
        viewParams.width = viewWidth
        viewParams.height = height

        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            onItemClickedListener.onItem(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(updatedItems: List<Book>) {
        items.clear()
        items.addAll(updatedItems)
        notifyDataSetChanged()
    }

    inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imageIv = view.findViewById<ImageView>(R.id.pt_book_image_iv)
        private val titleTv = view.findViewById<TextView>(R.id.pt_title)

        fun bind(item: Book) {
            with(item) {
                imageIv.loadImg(bigImgLink(), middleImgLink(), imgLink)
                titleTv.fadeInText(title)
            }
        }
    }
}
