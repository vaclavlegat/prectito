package cz.legat.prectito.ui.main.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cz.legat.prectito.R
import cz.legat.prectito.extensions.loadImg
import cz.legat.prectito.model.Book
import cz.legat.prectito.ui.main.base.BaseAdapter

class SearchResultsAdapter(private val onItemClickedListener: OnItemClickedListener<Book>) :
    BaseAdapter<Book>(onItemClickedListener) {

    override fun layout(): Int {
        return R.layout.pt_item_search_result
    }

    override fun viewHolder(view: View): BaseViewHolder {
        return BookViewHolder(view)
    }

    inner class BookViewHolder(view: View) : BaseViewHolder(view) {

        private val authorTv = view.findViewById<TextView>(R.id.pt_book_author_tv)
        private val titleTv = view.findViewById<TextView>(R.id.pt_book_title_tv)
        private val imageIv = view.findViewById<ImageView>(R.id.pt_book_image_iv)

        override fun bind(item: Book) {
            with(item) {
                var author = ""
                var published = ""

                val split = description.split(",")
                if (split.size == 2) {
                    author = split[1].trim()
                    published = split[0].trim()
                } else {
                    author = split[0]
                }
                authorTv.text = "$author - ($published)"
                titleTv.text = title
                imageIv.loadImg(imgLink)
            }
        }
    }
}
