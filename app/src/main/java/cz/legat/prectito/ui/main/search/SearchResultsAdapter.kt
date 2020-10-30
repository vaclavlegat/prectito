package cz.legat.prectito.ui.main.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cz.legat.prectito.R
import cz.legat.core.model.SearchResult
import cz.legat.core.extensions.loadImg
import cz.legat.core.base.BaseAdapter

class SearchResultsAdapter(private val onItemClickedListener: OnItemClickedListener<SearchResult>) :
    BaseAdapter<SearchResult>(onItemClickedListener) {

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

        override fun bind(item: SearchResult, position: Int) {
            with(item) {
                authorTv.text = getResultSubtitle()
                titleTv.text = getResultTitle()
                imageIv.loadImg(getResultImgLink())
            }
        }
    }
}
