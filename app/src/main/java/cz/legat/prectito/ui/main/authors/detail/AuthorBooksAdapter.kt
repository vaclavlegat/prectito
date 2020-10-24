package cz.legat.prectito.ui.main.authors.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cz.legat.prectito.R
import cz.legat.core.model.Book
import cz.legat.core.model.bigImgLink
import cz.legat.core.model.middleImgLink
import cz.legat.core.extensions.loadImg

class AuthorBooksAdapter(private val onItemClickedListener: OnBookClickedListener) : PagedListAdapter<Book, AuthorBooksAdapter.BookViewHolder>(DIFF_CALLBACK) {

    interface OnBookClickedListener {
        fun onBook(book: Book)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pt_item_search_result, parent, false) as View
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val author = getItem(position)
        author?.let {
            holder.bind(it)
        }
    }

    inner class BookViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val authorTv = view.findViewById<TextView>(R.id.pt_book_author_tv)
        private val titleTv = view.findViewById<TextView>(R.id.pt_book_title_tv)
        private val imageIv = view.findViewById<ImageView>(R.id.pt_book_image_iv)

        fun bind(item: Book) {
            with(item) {
                authorTv.text = author?.name
                titleTv.text = title
                imageIv.loadImg(bigImgLink(), middleImgLink(), imgLink)
            }
            view.setOnClickListener {
                onItemClickedListener.onBook(item)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id === newItem.id
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }
        }
    }
}
