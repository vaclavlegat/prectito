package cz.legat.authors.ui.detail

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import cz.legat.authors.databinding.PtDetailTabsFragmentBinding
import cz.legat.core.base.BaseAdapter
import cz.legat.core.extensions.*
import cz.legat.core.model.Book
import cz.legat.core.ui.BindingFragment
import cz.legat.navigation.BooksNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthorDetailFragment :
    BindingFragment<PtDetailTabsFragmentBinding>(PtDetailTabsFragmentBinding::inflate) {

    private val viewModel: AuthorDetailViewModel by viewModels()

    @Inject
    lateinit var navigator: BooksNavigator

    private val bookListener = object : BaseAdapter.OnItemClickedListener<Book> {
        override fun onItem(item: Book) {
            startActivity(navigator.getOpenDetailIntent(requireContext(), item.id))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val w = activity?.window
        w?.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val booksAdapter = BooksAdapter(bookListener)
        binding.ptBooksRv.simpleGrid(booksAdapter)

        viewModel.books.observe(viewLifecycleOwner, Observer {
            booksAdapter.update(it.take(3))
            binding.ptMoreBtn.visibleIf(it.size > 3)
        })

        viewModel.author.observe(viewLifecycleOwner, Observer { author ->
            binding.collapsing.fadeInText(author?.name)
            binding.ptAuthorImageIv.loadImg(author?.authorImgLink)
            binding.ptAuthorImageIv.loadWithBackground(author?.authorImgLink, binding.ptImageBg)
            binding.ptAuthorLifeTv.fadeInText(author?.life)
            binding.ptAuthorCvTv.fadeInText(author?.cv)
            binding.ptAuthorCvTv.setOnClickListener {
                author?.cv?.let { cv ->
                    findNavController().navigate(AuthorDetailFragmentDirections.toBioFragment(cv))
                }
            }
            binding.ptMoreBtn.setOnClickListener {
                author?.authorId?.let { id ->
                    findNavController().navigate(AuthorDetailFragmentDirections.toBooksFragment(id))
                }
            }
            binding.appbarLayout.addOnOffsetChangedListener(AppBarOffsetOffsetChangedListener(object :
                OnAppBarOffsetChangedListener {
                override fun onExpanded() {

                }

                override fun onCollapsed() {

                }

                override fun onIntermediate() {

                }
            }))

        })
    }
}
