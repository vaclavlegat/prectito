package cz.legat.books.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cz.legat.books.R
import cz.legat.core.model.Book
import cz.legat.core.model.bigImgLink
import cz.legat.core.model.middleImgLink
import cz.legat.core.extensions.loadImg
import cz.legat.core.base.BaseAdapter

class BooksAdapter(private val onItemClickedListener: OnItemClickedListener<Book>) :
    BaseAdapter<Book>(onItemClickedListener) {

    override fun layout(): Int {
        return R.layout.pt_item_book
    }

    override fun viewHolder(view: View): BaseViewHolder {
        return BookViewHolder(view)
    }

    inner class BookViewHolder(view: View) : BaseAdapter<Book>.BaseViewHolder(view) {

        private val imageIv = view.findViewById<ImageView>(R.id.pt_book_image_iv)

        override fun bind(item: Book, position: Int) {
            with(item) {
                imageIv.loadImg(bigImgLink(), middleImgLink(), imgLink)
            }
        }
    }
}
