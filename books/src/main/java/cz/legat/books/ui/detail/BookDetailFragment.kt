package cz.legat.books.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cz.legat.books.R
import cz.legat.books.databinding.PtBookDetailFragmentBinding
import cz.legat.core.base.BaseAdapter
import cz.legat.core.extensions.*
import cz.legat.core.model.Comment
import cz.legat.core.ui.BindingFragment
import cz.legat.navigation.AuthorsNavigator
import cz.legat.navigation.BooksNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class BookDetailFragment : BindingFragment<PtBookDetailFragmentBinding>(PtBookDetailFragmentBinding::inflate) {

    private val viewModel: BookDetailViewModel by viewModels()

    private var commentsAdapter: CommentsAdapter? = null

    @Inject lateinit var authorsNavigator: AuthorsNavigator
    @Inject lateinit var bookNavigator: BooksNavigator

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
                binding.ptBookTitleTv.fadeInText(book.title)
                binding.ptBookAuthorTv.fadeInText(book.author?.name)
                binding.ptBookPublishedTv.fadeInText(book.published)
                binding.ptBookDescTv.fadeInText(book.description)
                binding.ptBookImageIv.loadImg(book.imgLink)
                binding.ptBookAuthorTv.setOnClickListener {
                    book.author?.authorId?.let { authorId ->
                        startActivity(
                            authorsNavigator.getOpenDetailIntent(
                                requireContext(),
                                authorId
                            )
                        )
                    }
                }

                binding.appbarLayout.addOnOffsetChangedListener(
                    AppBarOffsetOffsetChangedListener(object : OnAppBarOffsetChangedListener{
                        override fun onExpanded() {
                            binding.collapsing.title = ""
                        }

                        override fun onCollapsed() {
                            binding.collapsing.fadeInText(book.title)
                        }

                        override fun onIntermediate() {
                            binding.collapsing.title = ""
                        }

                    }))

                binding.ptBookRatingTv.animateRating(book.rating?.toInt() ?: 0)
                binding.ptBookRatingTv.goneIf(book.ratingsCount.isNullOrEmpty())

                binding.ptBookLinkBtn.visibleIf(book.eBookLink != null)
                binding.ptBookLinkBtn.setOnClickListener {
                    viewModel.downloadPdf(book.eBookLink)
                }

                binding.ptBookDescTv.setOnClickListener {
                    findNavController().navigate(R.id.aboutFragment, bundleOf("about" to book.description))
                }
            }
        })

        viewModel.filePath.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                startActivity(bookNavigator.getOpenPdfIntent(requireContext(), it))
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
}
