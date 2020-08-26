package cz.legat.prectito.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cz.legat.prectito.R
import cz.legat.prectito.model.Book
import cz.legat.prectito.persistence.SavedBook

class MyBooksAdapter(val onBookClickedListener: OnBookClickedListener) : RecyclerView.Adapter<MyBooksAdapter.BookViewHolder>() {

    interface OnBookClickedListener {
        fun onBook(book: SavedBook)
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

    fun update(updatedBooks: List<SavedBook>) {
        books.clear()
        books.addAll(updatedBooks)
        notifyDataSetChanged()
    }

    inner class BookViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val authorTv = view.findViewById<TextView>(R.id.pt_book_author_tv)
        private val titleTv = view.findViewById<TextView>(R.id.pt_book_title_tv)
        private val subtitleTv = view.findViewById<TextView>(R.id.pt_book_subtitle_tv)

        internal fun bind(book: SavedBook) {
            view.setOnClickListener {
                onBookClickedListener.onBook(book)
            }
            with(book) {
                authorTv.text = author
                titleTv.text = title
                if(!subtitle.isNullOrEmpty()){
                    subtitleTv.text = subtitle
                    subtitleTv.visibility = View.VISIBLE
                } else {
                    subtitleTv.visibility = View.GONE
                }
            }
        }
    }
}
