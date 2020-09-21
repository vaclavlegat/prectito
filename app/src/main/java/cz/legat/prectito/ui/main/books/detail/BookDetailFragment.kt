package cz.legat.prectito.ui.main.books.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.core.model.Comment
import cz.legat.prectito.R
import cz.legat.prectito.extensions.fadeInText
import cz.legat.prectito.extensions.loadImg
import cz.legat.prectito.ui.main.base.BaseAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailFragment : Fragment() {

    private val args: BookDetailFragmentArgs by navArgs()
    private val viewModel: BookDetailViewModel by viewModels()

    lateinit var titleTv: TextView
    lateinit var authorTv: TextView
    lateinit var publishedTv: TextView
    lateinit var imageIv: ImageView
    lateinit var descTv: TextView
    lateinit var commentsRV: RecyclerView
    lateinit var moreCommentsBtn: Button
    private var commentsAdapter: CommentsAdapter? = null

    companion object {
        fun newInstance(): BookDetailFragment {
            return BookDetailFragment()
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
        commentsRV = view.findViewById(R.id.pt_comments_rv)
        moreCommentsBtn = view.findViewById(R.id.pt_more_comments_btn)
        moreCommentsBtn.setOnClickListener {
            val action = BookDetailFragmentDirections.actionBookDetailFragmentToBookCommentsFragment(args.id)
            findNavController().navigate(action)
        }

        commentsAdapter = CommentsAdapter(object : BaseAdapter.OnItemClickedListener<Comment> {
            override fun onItem(item: Comment) {
            }
        })

        commentsRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = commentsAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.book.observe(viewLifecycleOwner, Observer { book ->
            book?.let {
                titleTv.fadeInText(it.title)
                authorTv.fadeInText(it.author?.name)
                publishedTv.fadeInText(it.published)
                descTv.fadeInText(it.description)
                imageIv.loadImg(it.imgLink)
            }
        })

        viewModel.comments.observe(viewLifecycleOwner, Observer<List<Comment>> {
            commentsAdapter?.update(it)
        })

        viewModel.showMoreCommentsVisible.observe(viewLifecycleOwner, Observer {
            if (it) {
                moreCommentsBtn.visibility = View.VISIBLE
            }  else {
                moreCommentsBtn.visibility = View.GONE
            }
        })
    }
}
