package cz.legat.authors.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cz.legat.authors.R
import cz.legat.core.model.Author
import cz.legat.core.extensions.loadImg

class AuthorsAdapter(val onAuthorClickedListener: OnAuthorClickedListener): PagingDataAdapter<Author, AuthorsAdapter.AuthorViewHolder>(DIFF_CALLBACK) {

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
        private val ivImage = view.findViewById<ImageView>(R.id.iv_author_image)
        private val layout = view

        internal fun bind(author: Author) {
            with(author) {
                tvName.text = name
                authorImgLink.let {
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
