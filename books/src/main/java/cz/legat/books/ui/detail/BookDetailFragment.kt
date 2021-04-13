package cz.legat.books.ui.detail

import android.Manifest
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.core.view.doOnLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import cz.legat.books.R
import cz.legat.books.databinding.PtBookDetailFragmentBinding
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
        val tabsAdapter = TabsAdapter(this)
        with(binding){
            ptAppbar.applySystemWindowInsetsToPadding(top = true, left = true, right = true)

            pager.adapter = tabsAdapter
            TabLayoutMediator(tabLayout, pager) { tab, position ->
                tab.text = fragmentTitles()[position]
            }.attach()

            ptToolbar.setNavigationOnClickListener {
                requireActivity().finish()
            }

            viewModel.book.observe(viewLifecycleOwner, Observer { book ->
                book?.let {
                    ptBookTitleTv.fadeInTextFromStart(book.title)
                    ptBookAuthorTv.fadeInText(book.author?.name)
                    ptBookPublishedTv.fadeInText(book.published)
                    ptBookImageIv.loadImg(book.imgLink)
                    ptBookAuthorTv.setOnClickListener {
                        book.author?.authorId?.let { authorId ->
                            startActivity(
                                authorsNavigator.getOpenDetailIntent(
                                    requireContext(),
                                    authorId
                                )
                            )
                        }
                    }

                    ptBookRatingTv.animateRating(book.rating?.toInt() ?: 0)

                    ptBookLinkBtn.visibleIf(book.eBookLink != null)
                    ptBookLinkBtn.setOnClickListener {
                        Dexter.withContext(requireContext())
                            .withPermissions(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                            .withListener(object : MultiplePermissionsListener {
                                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                                    viewModel.downloadPdf(book.eBookLink)
                                }

                                override fun onPermissionRationaleShouldBeShown(
                                    p0: MutableList<PermissionRequest>?,
                                    p1: PermissionToken?
                                ) {

                                }


                            }).check()
                    }
                }
            })

            viewModel.filePath.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    startActivity(bookNavigator.getOpenPdfIntent(requireContext(), it))
                }
            })

            ptAppbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                val progress = -verticalOffset / (appBarLayout?.totalScrollRange?.toFloat() ?: 0f)
                motion.progress = progress
                motion.translationY = -verticalOffset.toFloat()
                ptBookRatingTv.goneIf(viewModel.book.value?.ratingsCount.isNullOrEmpty())
                val result = ColorUtils.blendARGB(
                    getThemeColor(R.attr.colorPrimary),
                    getThemeColor(R.attr.colorPrimaryVariant),
                    progress
                )
                ptAppbar.setBackgroundColor(result)
            })

            motion.apply {
                doOnLayout {
                    val toolbarHeight = ptToolbar.measuredHeight
                    val tabsHeight = tabLayout.measuredHeight

                    val requiredChildHeight =
                        toolbarHeight + tabsHeight + ptBookImageHolderIv.measuredHeight + context.dpToPx(
                            16
                        )
                    val minimumChildHeight = toolbarHeight + tabsHeight

                    updateLayoutParams<ViewGroup.LayoutParams> { height = requiredChildHeight }
                    minimumHeight = minimumChildHeight
                }
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
