package cz.legat.prectito.ui.main.books.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cz.legat.prectito.R
import cz.legat.prectito.model.Comment
import cz.legat.prectito.extensions.tintedDrawable

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    private var comments = mutableListOf<Comment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pt_item_book_comment, parent, false) as View
        return CommentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    fun update(newComments: List<Comment>) {
        val sorted = newComments.sortedByDescending { it.likes }
        val diffCallback = CommentListDiffCallback(comments, sorted)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        comments.clear()
        comments.addAll(sorted)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvUser = view.findViewById<TextView>(R.id.pt_comment_user_tv)
        private val tvComment = view.findViewById<TextView>(R.id.pt_comment_text_tv)
        private val tvDate = view.findViewById<TextView>(R.id.pt_comment_date_tv)
        private val ivAvatar = view.findViewById<ImageView>(R.id.pt_comment_avatar_iv)
        private val llRatingHolder = view.findViewById<LinearLayout>(R.id.pt_comment_rating_holder_ll)
        private val layout = view

        internal fun bind(c: Comment) {
            with(c) {
                tvUser.text = user
                tvComment.text = comment
                tvDate.text = date
                Glide.with(layout.context)
                    .load(avatarLink)
                    .centerCrop()
                    .into(ivAvatar)

                llRatingHolder.removeAllViews()

                if (rating > 0) {
                    for (i in 1..rating) {
                        val star = ImageView(layout.context)
                        star.tintedDrawable(R.drawable.pt_star_icon, R.color.colorAccent)
                        star.layoutParams = LinearLayout.LayoutParams(32, 32)
                        llRatingHolder.addView(star)
                    }
                } else {
                    val trash = TextView(layout.context)
                    trash.text = layout.context.getString(R.string.pt_rating_trash)
                    trash.setTextColor(ContextCompat.getColor(layout.context, R.color.white))
                    trash.textSize = 12f
                    llRatingHolder.addView(trash)
                }
            }
        }
    }

    inner class CommentListDiffCallback(
        private val oldList: List<Comment>,
        private val newList: List<Comment>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id === newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            return oldList[oldPosition] == newList[newPosition]
        }
    }

}
