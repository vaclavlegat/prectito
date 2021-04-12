package cz.legat.books.ui.detail

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.core.view.doOnLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import cz.legat.books.R
import cz.legat.books.databinding.PtBookDetailFragmentBinding
import cz.legat.books.ui.BooksFragment
import cz.legat.books.ui.NEW
import cz.legat.books.ui.POPULAR
import cz.legat.core.extensions.*
import cz.legat.core.ui.BindingFragment
import cz.legat.navigation.AuthorsNavigator
import cz.legat.navigation.BooksNavigator
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import javax.inject.Inject


@AndroidEntryPoint
class BookDetailFragment :
    BindingFragment<PtBookDetailFragmentBinding>(PtBookDetailFragmentBinding::inflate) {

    private val viewModel: BookDetailViewModel by viewModels()

    @Inject
    lateinit var authorsNavigator: AuthorsNavigator

    @Inject
    lateinit var bookNavigator: BooksNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ptAppbar.applySystemWindowInsetsToPadding(top = true, left = true, right = true)
        val tabsAdapter = TabsAdapter(this)
        binding.pager.adapter = tabsAdapter
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, binding.pager) { tab, position ->
            tab.text = fragmentTitles()[position]
        }.attach()

        binding.ptToolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }

        viewModel.book.observe(viewLifecycleOwner, Observer { book ->
            book?.let {
                binding.ptBookTitleTv.fadeInTextFromStart(book.title)
                binding.ptBookAuthorTv.fadeInText(book.author?.name)
                binding.ptBookPublishedTv.fadeInText(book.published)
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

                binding.ptBookRatingTv.animateRating(book.rating?.toInt() ?: 0)
            }
        })

        binding.ptAppbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val progress = -verticalOffset / (appBarLayout?.totalScrollRange?.toFloat() ?: 0f)
            binding.motion.progress = progress
            binding.motion.translationY = -verticalOffset.toFloat()
            binding.ptBookRatingTv.goneIf(viewModel.book.value?.ratingsCount.isNullOrEmpty())
            val result = ColorUtils.blendARGB(getThemeColor(R.attr.colorPrimary), getThemeColor(R.attr.colorPrimaryVariant), progress)
            binding.ptAppbar.setBackgroundColor(result)
        })

        binding.motion.apply {
            doOnLayout {
                val toolbarHeight = binding.ptToolbar.measuredHeight
                val tabsHeight = binding.tabLayout.measuredHeight

                val requiredChildHeight = toolbarHeight + tabsHeight + binding.ptBookImageHolderIv.measuredHeight + context.dpToPx(16)
                val minimumChildHeight = toolbarHeight + tabsHeight

                updateLayoutParams<ViewGroup.LayoutParams> { height = requiredChildHeight }
                minimumHeight = minimumChildHeight
            }
        }
    }

    private fun fragmentTitles(): List<String> {
        return listOf(
            getString(R.string.pt_about),
            getString(R.string.pt_comments)
        )
    }

    private fun fragments(): List<Fragment> {
        return listOf(BookDetailAboutFragment().apply {
            arguments = Bundle().apply {
                putString("id", viewModel.bookId)
            }
        }, BookCommentsFragment().apply {
            arguments = Bundle().apply {
                putString("id", viewModel.bookId)
            }
        })
    }

    inner class TabsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = fragments().size

        override fun createFragment(position: Int): Fragment {
            return fragments()[position]
        }
    }
}
