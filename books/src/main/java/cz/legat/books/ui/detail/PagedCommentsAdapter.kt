package cz.legat.books.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cz.legat.books.R
import cz.legat.core.extensions.fadeInText
import cz.legat.core.extensions.loadImg
import cz.legat.core.extensions.tintedDrawable
import cz.legat.core.model.Comment

class PagedCommentsAdapter(val onCommentClickedListener: OnCommentClickedListener) :
    PagingDataAdapter<Comment, PagedCommentsAdapter.CommentsViewHolder>(DIFF_CALLBACK) {

    interface OnCommentClickedListener {
        fun onComment(comment: Comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pt_item_book_comment, parent, false) as View
        return CommentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val author = getItem(position)
        author?.let {
            holder.bind(it, position)
        }
    }

    inner class CommentsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvUser = view.findViewById<TextView>(R.id.pt_comment_user_tv)
        private val tvComment = view.findViewById<TextView>(R.id.pt_comment_text_tv)
        private val tvDate = view.findViewById<TextView>(R.id.pt_comment_date_tv)
        private val ivAvatar = view.findViewById<ImageView>(R.id.pt_comment_avatar_iv)
        private val llRatingHolder =
            view.findViewById<LinearLayout>(R.id.pt_comment_rating_holder_ll)
        private val layout = view

        fun bind(c: Comment, position: Int) {
            with(c) {
                tvUser.fadeInText(c.user)
                tvComment.fadeInText(c.comment)
                tvDate.fadeInText(c.date)
                ivAvatar.loadImg(avatarLink)
                llRatingHolder.removeAllViews()

                if (rating > 0) {
                    for (i in 1..rating) {
                        val star = ImageView(layout.context)
                        star.tintedDrawable(R.drawable.pt_star_icon, R.attr.colorPrimary)
                        star.layoutParams = LinearLayout.LayoutParams(32, 32)
                        llRatingHolder.addView(star)
                    }
                } else if (rating == 0) {
                    val trash = TextView(layout.context)
                    trash.fadeInText(layout.context.getString(R.string.pt_rating_trash))
                    trash.setTextColor(ContextCompat.getColor(layout.context, R.color.white))
                    trash.textSize = 12f
                    llRatingHolder.addView(trash)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id === newItem.id
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }
        }
    }
}
