package cz.legat.prectito.ui.main.books.detail

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import cz.legat.prectito.R
import cz.legat.core.model.Comment
import cz.legat.prectito.extensions.fadeInText
import cz.legat.prectito.extensions.loadImg
import cz.legat.prectito.extensions.tintedDrawable
import cz.legat.prectito.ui.main.base.BaseAdapter

class CommentsAdapter(private val onItemClickedListener: OnItemClickedListener<Comment>) :
    BaseAdapter<Comment>(onItemClickedListener) {

    override fun layout(): Int {
        return R.layout.pt_item_book_comment
    }

    override fun viewHolder(view: View): BaseViewHolder {
        return CommentViewHolder(view)
    }

    inner class CommentViewHolder(view: View) : BaseViewHolder(view) {

        private val tvUser = view.findViewById<TextView>(R.id.pt_comment_user_tv)
        private val tvComment = view.findViewById<TextView>(R.id.pt_comment_text_tv)
        private val tvDate = view.findViewById<TextView>(R.id.pt_comment_date_tv)
        private val ivAvatar = view.findViewById<ImageView>(R.id.pt_comment_avatar_iv)
        private val llRatingHolder =
            view.findViewById<LinearLayout>(R.id.pt_comment_rating_holder_ll)
        private val layout = view

        override fun bind(c: Comment) {
            with(c) {
                tvUser.fadeInText(c.user)
                tvComment.fadeInText(c.comment)
                tvDate.fadeInText(c.date)
                ivAvatar.loadImg(avatarLink)
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
                    trash.fadeInText(layout.context.getString(R.string.pt_rating_trash))
                    trash.setTextColor(ContextCompat.getColor(layout.context, R.color.white))
                    trash.textSize = 12f
                    llRatingHolder.addView(trash)
                }
            }
        }
    }
}
