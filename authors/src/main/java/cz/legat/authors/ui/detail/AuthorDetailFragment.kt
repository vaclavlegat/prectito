package cz.legat.authors.ui.detail

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import cz.legat.authors.R
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val booksAdapter = BooksAdapter(bookListener)
        binding.ptBooksRv.simpleLinear(booksAdapter)

        viewModel.books.observe(viewLifecycleOwner, Observer {
            booksAdapter.update(it)
            binding.ptAllBooksBtn.visibleIf(it.isNotEmpty())
        })

        viewModel.author.observe(viewLifecycleOwner, Observer { author ->
            binding.ptSearchBtn.setOnClickListener{
                //findNavController().navigate(R.id.searchFragment)
            }
            binding.ptAuthorImageIv.loadImg(author?.authorImgLink)
            //binding.ptAuthorImageIv.loadWithBackground(author?.authorImgLink, binding.ptImageBg)
            binding.ptAuthorNameTv.fadeInText(author?.name)
            binding.ptAuthorLifeTv.fadeInText(author?.life)
            binding.ptAuthorCvTv.fadeInText(author?.cv)
            binding.ptAuthorCvTv.setOnClickListener {
                author?.cv?.let { cv ->
                    findNavController().navigate(AuthorDetailFragmentDirections.toBioFragment(cv))
                }
            }
            binding.ptAllBooksBtn.setOnClickListener {
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

        binding.toolbar.setNavigationOnClickListener {
            activity?.finish()
        }
    }
}
