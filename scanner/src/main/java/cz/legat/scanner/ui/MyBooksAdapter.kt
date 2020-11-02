package cz.legat.scanner.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.legat.core.persistence.SavedBook
import cz.legat.scanner.R

class MyBooksAdapter(val onBookClickedListener: OnBookClickedListener) : RecyclerView.Adapter<MyBooksAdapter.BookViewHolder>() {

    interface OnBookClickedListener {
        fun onBook(book: SavedBook)
        fun onBookRemoved(book: SavedBook)
    }

    private var books = mutableListOf<SavedBook>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pt_item_saved_book, parent, false) as View
        return BookViewHolder(view)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    fun removeAt(position: Int) {
        val book = books[position]
        books.removeAt(position)
        notifyItemRemoved(position)
        onBookClickedListener.onBookRemoved(book)
    }

    fun update(updatedBooks: List<SavedBook>) {
        books.clear()
        books.addAll(updatedBooks)
        notifyDataSetChanged()
    }

    inner class BookViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val authorTv = view.findViewById<TextView>(R.id.pt_book_author_tv)
        private val titleTv = view.findViewById<TextView>(R.id.pt_book_title_tv)
        private val publishedTV = view.findViewById<TextView>(R.id.pt_book_published_tv)

        internal fun bind(book: SavedBook) {
            view.setOnClickListener {
                onBookClickedListener.onBook(book)
            }
            with(book) {
                authorTv.text = author
                titleTv.text = title
                if(!subtitle.isNullOrEmpty()){
                    titleTv.text = "$title - $subtitle"
                }
                publishedTV.text = publishedDate?.take(4)
            }
        }
    }
}
