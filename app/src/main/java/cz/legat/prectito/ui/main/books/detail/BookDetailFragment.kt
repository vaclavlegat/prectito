package cz.legat.prectito.ui.main.books.detail

import android.animation.ValueAnimator
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
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

    lateinit var titleTV: TextView
    lateinit var authorTV: TextView
    lateinit var publishedTV: TextView
    lateinit var imageIV: ImageView
    lateinit var descTV: TextView
    lateinit var ratingTV: TextView
    lateinit var commentsRV: RecyclerView
    lateinit var moreCommentsBtn: Button
    lateinit var commentsTitleTV: TextView
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
        titleTV = view.findViewById(R.id.pt_book_title_tv)
        authorTV = view.findViewById(R.id.pt_book_author_tv)
        publishedTV = view.findViewById(R.id.pt_book_published_tv)
        imageIV = view.findViewById(R.id.pt_book_image_iv)
        descTV = view.findViewById(R.id.pt_book_desc_tv)
        ratingTV = view.findViewById(R.id.pt_book_rating_tv)
        commentsRV = view.findViewById(R.id.pt_comments_rv)
        commentsTitleTV = view.findViewById(R.id.pt_comments_title_tv)
        moreCommentsBtn = view.findViewById(R.id.pt_more_comments_btn)
        moreCommentsBtn.setOnClickListener {
            val action =
                BookDetailFragmentDirections.actionBookDetailFragmentToBookCommentsFragment(args.id)
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

        commentsRV.isNestedScrollingEnabled = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.book.observe(viewLifecycleOwner, Observer { book ->
            book?.let {
                titleTV.fadeInText(it.title)
                authorTV.fadeInText(it.author?.name)
                publishedTV.fadeInText(it.published)
                descTV.fadeInText(it.description)

                ratingTV.fadeInText("0 %")
                ValueAnimator.ofInt(it.rating?.toInt() ?: 0).apply {
                    addUpdateListener { animation ->
                        ratingTV.text = "${animation.animatedValue as Int} %"
                    }
                    duration = 1000
                    start()
                }

                val ratingBG = ratingTV.background
                if (ratingBG is GradientDrawable) {
                    ratingBG.setColor(
                        ContextCompat.getColor(
                            requireContext(),
                            getRatingColor(it.rating)
                        )
                    )
                }
                ratingTV.visibility = if (book.ratingsCount.isNullOrEmpty()) View.GONE else View.VISIBLE
                imageIV.loadImg(it.imgLink)
            }
        })

        viewModel.comments.observe(viewLifecycleOwner, Observer<List<Comment>> {
            if(it.isEmpty()){
                commentsTitleTV.visibility = View.GONE
            } else {
                commentsTitleTV.visibility = View.VISIBLE
            }

            commentsAdapter?.update(it)
        })

        viewModel.showMoreCommentsVisible.observe(viewLifecycleOwner, Observer {
            if (it) {
                moreCommentsBtn.visibility = View.VISIBLE
            } else {
                moreCommentsBtn.visibility = View.GONE
            }
        })
    }

    private fun getRatingColor(rating: String?): Int {
        return when (rating?.toInt()) {
            in 0..30 -> R.color.pt_rating_30
            in 31..50 -> R.color.pt_rating_50
            in 51..80 -> R.color.pt_rating_70
            in 81..90 -> R.color.pt_rating_80
            else -> {
                R.color.pt_rating_100
            }
        }
    }
}
