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
import cz.legat.prectito.databinding.PtBookDetailFragmentBinding
import cz.legat.prectito.extensions.fadeInText
import cz.legat.prectito.extensions.goneIf
import cz.legat.prectito.extensions.loadImg
import cz.legat.prectito.extensions.visibleIf
import cz.legat.prectito.ui.main.BindingFragment
import cz.legat.prectito.ui.main.base.BaseAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailFragment : BindingFragment<PtBookDetailFragmentBinding>() {

    private val args: BookDetailFragmentArgs by navArgs()
    private val viewModel: BookDetailViewModel by viewModels()

    private var commentsAdapter: CommentsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PtBookDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.ptMoreCommentsBtn.setOnClickListener {
            val action =
                BookDetailFragmentDirections.actionBookDetailFragmentToBookCommentsFragment(args.id)
            findNavController().navigate(action)
        }

        commentsAdapter = CommentsAdapter(object : BaseAdapter.OnItemClickedListener<Comment> {
            override fun onItem(item: Comment) {
            }
        })

        binding.ptCommentsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = commentsAdapter
            isNestedScrollingEnabled = false
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.book.observe(viewLifecycleOwner, Observer { book ->
            book?.let {
                binding.ptBookTitleTv.fadeInText(it.title)
                binding.ptBookAuthorTv.fadeInText(it.author?.name)
                binding.ptBookPublishedTv.fadeInText(it.published)
                binding.ptBookDescTv.fadeInText(it.description)

                binding.ptBookRatingTv.fadeInText("0 %")
                ValueAnimator.ofInt(it.rating?.toInt() ?: 0).apply {
                    addUpdateListener { animation ->
                        binding.ptBookRatingTv.text = "${animation.animatedValue as Int} %"
                    }
                    duration = 1000
                    start()
                }

                val ratingBG = binding.ptBookRatingTv.background
                if (ratingBG is GradientDrawable) {
                    ratingBG.setColor(
                        ContextCompat.getColor(
                            requireContext(),
                            getRatingColor(it.rating)
                        )
                    )
                }
                binding.ptBookRatingTv.goneIf(book.ratingsCount.isNullOrEmpty())
                binding.ptBookImageIv.loadImg(it.imgLink)
            }
        })

        viewModel.comments.observe(viewLifecycleOwner, Observer<List<Comment>> {
            binding.ptCommentsTitleTv.goneIf(it.isEmpty())
            commentsAdapter?.update(it)
        })

        viewModel.showMoreCommentsVisible.observe(viewLifecycleOwner, Observer {
            binding.ptMoreCommentsBtn.visibleIf(it)
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
