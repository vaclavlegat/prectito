package cz.legat.prectito.ui.main.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cz.legat.prectito.R
import cz.legat.prectito.model.Book

class SearchResultsAdapter(val onBookClickedListener: OnBookClickedListener) :
    RecyclerView.Adapter<SearchResultsAdapter.BookViewHolder>() {

    interface OnBookClickedListener {
        fun onBook(book: Book)
    }

    private var books = mutableListOf<Book>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pt_item_search_result, parent, false) as View
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

    inner class BookViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val authorTv = view.findViewById<TextView>(R.id.pt_book_author_tv)
        private val titleTv = view.findViewById<TextView>(R.id.pt_book_title_tv)
        private val imageIv = view.findViewById<ImageView>(R.id.pt_book_image_iv)

        internal fun bind(book: Book) {
            view.setOnClickListener {
                onBookClickedListener.onBook(book)
            }
            with(book) {
                var author = ""
                var published = ""

                val split = book.description.split(",")
                if (split.size == 2) {
                    author = split[1].trim()
                    published = split[0].trim()
                } else {
                    author = split[0]
                }
                authorTv.text = "$author - ($published)"
                titleTv.text = title
                Glide.with(imageIv).load(imgLink).into(imageIv)
            }
        }
    }
}
