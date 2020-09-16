package cz.legat.prectito.ui.main.books

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cz.legat.prectito.R
import cz.legat.prectito.extensions.loadImg
import cz.legat.prectito.model.Book
import cz.legat.prectito.model.bigImgLink
import cz.legat.prectito.model.middleImgLink
import cz.legat.prectito.ui.main.base.BaseAdapter

class BooksAdapter(private val onItemClickedListener: OnItemClickedListener<Book>) :
    BaseAdapter<Book>(onItemClickedListener) {

    override fun layout(): Int {
        return R.layout.pt_item_book
    }

    override fun viewHolder(view: View): BaseViewHolder {
        return BookViewHolder(view)
    }

    inner class BookViewHolder(view: View) : BaseAdapter<Book>.BaseViewHolder(view) {

        private val authorTv = view.findViewById<TextView>(R.id.pt_book_author_tv)
        private val titleTv = view.findViewById<TextView>(R.id.pt_book_title_tv)
        private val imageIv = view.findViewById<ImageView>(R.id.pt_book_image_iv)

        override fun bind(item: Book) {
            with(item) {
                authorTv.text = author?.name
                titleTv.text = title
                imageIv.loadImg(bigImgLink(), middleImgLink(), imgLink)
            }
        }
    }
}
