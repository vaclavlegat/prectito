package cz.legat.prectito.ui.main.books.detail

import android.animation.ValueAnimator
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import cz.legat.core.base.BaseAdapter
import cz.legat.core.extensions.ID_KEY
import cz.legat.core.extensions.fadeInText
import cz.legat.core.extensions.goneIf
import cz.legat.core.extensions.loadImg
import cz.legat.core.extensions.loadSingleBlurredImg
import cz.legat.core.extensions.visibleIf
import cz.legat.core.model.Comment
import cz.legat.core.ui.BindingFragment
import cz.legat.prectito.R
import cz.legat.prectito.databinding.PtBookDetailFragmentBinding
import cz.legat.prectito.navigation.goToAuthorDetailIntent
import dagger.hilt.android.AndroidEntryPoint

private enum class CollapsingToolbarLayoutState {
    EXPANDED, COLLAPSED, INTERNEDIATE
}

@AndroidEntryPoint
class BookDetailFragment : BindingFragment<PtBookDetailFragmentBinding>(PtBookDetailFragmentBinding::inflate) {

    private val viewModel: BookDetailViewModel by viewModels()

    private var commentsAdapter: CommentsAdapter? = null

    private var state: CollapsingToolbarLayoutState = CollapsingToolbarLayoutState.EXPANDED

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val id = arguments?.getString(ID_KEY) ?: throw IllegalArgumentException()
        binding.ptMoreCommentsBtn.setOnClickListener {
            findNavController().navigate(R.id.bookCommentsFragment, bundleOf(ID_KEY to id))
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

                binding.appbarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
                    if (verticalOffset == 0) {
                        if (state !== CollapsingToolbarLayoutState.EXPANDED) {
                            state = CollapsingToolbarLayoutState.EXPANDED //Modify the status token to expand
                            binding.collapsing.title = "" //Set title to EXPANDED
                            //activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                        }
                    } else if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange) {
                        if (state !== CollapsingToolbarLayoutState.COLLAPSED) {
                            binding.collapsing.fadeInText(it.title) //Set title not to display
                            //activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.black)
                            state = CollapsingToolbarLayoutState.COLLAPSED //Modified status marked as folded
                        }
                    } else {
                        if (state !== CollapsingToolbarLayoutState.INTERNEDIATE) {
                            if (state === CollapsingToolbarLayoutState.COLLAPSED) {
                                //Hide Play Button When Changed from Folding to Intermediate State
                            }
                            //activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                            binding.collapsing.title = "" //Set title to INTERNEDIATE
                            state = CollapsingToolbarLayoutState.INTERNEDIATE //Modify the status tag to the middle
                        }
                    }
                })

                binding.ptBookAuthorTv.setOnClickListener {
                    book.author?.authorId?.let { authorId ->
                        startActivity(requireContext().goToAuthorDetailIntent(authorId, book.author?.name!!, book.author?.authorImgLink!!))
                    }
                }

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
                binding.ptBookImageBgStartIv?.loadSingleBlurredImg(it.imgLink)
                binding.ptBookImageBgMiddleIv?.loadSingleBlurredImg(it.imgLink)
                binding.ptBookImageBgEndIv?.loadSingleBlurredImg(it.imgLink)
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
