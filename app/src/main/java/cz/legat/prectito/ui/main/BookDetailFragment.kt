package cz.legat.prectito.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import cz.legat.prectito.R
import cz.legat.prectito.model.Book
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BookDetailFragment : Fragment() {

    private val viewModel: BookDetailViewModel by viewModels()

    lateinit var titleTv: TextView
    lateinit var imageIv: ImageView
    lateinit var descTv: TextView

    companion object {
        fun newInstance(book: Book) : BookDetailFragment {
            return BookDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("book", book)
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pt_book_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        titleTv = view.findViewById(R.id.pt_book_title_tv)
        imageIv = view.findViewById(R.id.pt_book_image_iv)
        descTv = view.findViewById(R.id.pt_book_desc_tv)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val book = arguments?.getParcelable<Book>("book")

        titleTv.text = book?.title
        descTv.text = book?.description
        Glide.with(this).load(book?.imgLink).into(imageIv)

    }
}
