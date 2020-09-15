package cz.legat.prectito.ui.main.authors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import cz.legat.prectito.R
import cz.legat.prectito.extensions.loadImg
import cz.legat.prectito.model.Author
import cz.legat.prectito.model.Book
import cz.legat.prectito.ui.main.books.BooksAdapter

class AuthorsAdapter(val onAuthorClickedListener: OnAuthorClickedListener): PagedListAdapter<Author, AuthorsAdapter.AuthorViewHolder>(DIFF_CALLBACK) {

    interface OnAuthorClickedListener {
        fun onAuthor(author: Author)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pt_item_author, parent, false) as View
        return AuthorViewHolder(view)
    }

    override fun onBindViewHolder(holder: AuthorViewHolder, position: Int) {
        val author = getItem(position)
        author?.let {
            holder.bind(it)
        }
    }

    inner class AuthorViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvName = view.findViewById<TextView>(R.id.tv_author_name)
        private val tvLife = view.findViewById<TextView>(R.id.tv_author_life)
        private val ivImage = view.findViewById<ImageView>(R.id.iv_author_image)
        private val layout = view

        internal fun bind(author: Author) {
            with(author) {
                tvName.text = name
                tvLife.text = life
                authorImgLink?.let {
                    ivImage.loadImg(it)
                }
            }

            layout.setOnClickListener {
                onAuthorClickedListener.onAuthor(author)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Author>() {
            override fun areItemsTheSame(oldItem: Author, newItem: Author): Boolean {
                return oldItem.authorId === newItem.authorId
            }

            override fun areContentsTheSame(oldItem: Author, newItem: Author): Boolean {
                return oldItem == newItem
            }
        }
    }
}
