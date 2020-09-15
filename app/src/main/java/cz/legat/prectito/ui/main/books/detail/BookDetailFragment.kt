package cz.legat.prectito.ui.main.books.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import cz.legat.prectito.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailFragment : Fragment() {

    private val viewModel: BookDetailViewModel by viewModels()

    lateinit var titleTv: TextView
    lateinit var authorTv: TextView
    lateinit var publishedTv: TextView
    lateinit var imageIv: ImageView
    lateinit var descTv: TextView

    companion object {
        fun newInstance(id: String): BookDetailFragment {
            return BookDetailFragment().apply {
                arguments = Bundle().apply {
                    putString("id", id)
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
        authorTv = view.findViewById(R.id.pt_book_author_tv)
        publishedTv = view.findViewById(R.id.pt_book_published_tv)
        imageIv = view.findViewById(R.id.pt_book_image_iv)
        descTv = view.findViewById(R.id.pt_book_desc_tv)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.book.observe(viewLifecycleOwner, Observer { book ->
            book?.let {
                titleTv.text = it.title
                authorTv.text = it.author?.name
                publishedTv.text = it.published
                descTv.text = it.description
                Glide.with(requireActivity()).load(it.imgLink).into(imageIv)
            }
        })
    }
}
