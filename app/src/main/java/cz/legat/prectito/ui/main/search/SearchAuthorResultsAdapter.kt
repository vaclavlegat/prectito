package cz.legat.prectito.ui.main.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cz.legat.prectito.R
import cz.legat.prectito.extensions.loadImg
import cz.legat.prectito.model.Author
import cz.legat.prectito.model.Book
import cz.legat.prectito.ui.main.base.BaseAdapter

class SearchAuthorResultsAdapter(private val onItemClickedListener: OnItemClickedListener<Author>) :
    BaseAdapter<Author>(onItemClickedListener) {

    override fun layout(): Int {
        return R.layout.pt_item_search_result
    }

    override fun viewHolder(view: View): BaseViewHolder {
        return AuthorViewHolder(view)
    }

    inner class AuthorViewHolder(view: View) : BaseViewHolder(view) {

        private val authorTv = view.findViewById<TextView>(R.id.pt_book_author_tv)
        private val titleTv = view.findViewById<TextView>(R.id.pt_book_title_tv)
        private val imageIv = view.findViewById<ImageView>(R.id.pt_book_image_iv)

        override fun bind(item: Author) {
            with(item) {
                authorTv.text = life
                titleTv.text = name
                imageIv.loadImg(authorImgLink)
            }
        }
    }
}
