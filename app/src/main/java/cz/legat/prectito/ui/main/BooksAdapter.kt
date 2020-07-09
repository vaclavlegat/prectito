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

class BooksAdapter : RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    private var books = mutableListOf<Book>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pt_item_book, parent, false) as View
        return BookViewHolder(view)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    fun update(updatedBooks: List<Book>) {
        books.clear()
        books.addAll(updatedBooks)
        notifyDataSetChanged()
    }

    inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val authorTv = view.findViewById<TextView>(R.id.pt_book_author_tv)
        private val titleTv = view.findViewById<TextView>(R.id.pt_book_title_tv)
        private val imageIv = view.findViewById<ImageView>(R.id.pt_book_image_iv)

        internal fun bind(book: Book) {
            with(book) {
                authorTv.text = author?.name
                titleTv.text = title
                Glide.with(imageIv).load(imgLink).into(imageIv);
            }
        }
    }
}
