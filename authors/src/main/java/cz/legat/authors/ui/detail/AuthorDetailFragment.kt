package cz.legat.authors.ui.detail

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
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
import cz.legat.authors.R
import cz.legat.authors.databinding.PtDetailTabsFragmentBinding
import cz.legat.core.extensions.dpToPx
import cz.legat.core.extensions.fadeInText
import cz.legat.core.extensions.getThemeColor
import cz.legat.core.extensions.loadImg
import cz.legat.core.ui.BindingFragment
import cz.legat.navigation.BooksNavigator
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AuthorDetailFragment :
    BindingFragment<PtDetailTabsFragmentBinding>(PtDetailTabsFragmentBinding::inflate) {

    private val viewModel: AuthorDetailViewModel by viewModels()

    @Inject
    lateinit var navigator: BooksNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ptAppbar.applySystemWindowInsetsToPadding(top = true, left = true, right = true)
        val tabsAdapter = TabsAdapter(this)
        binding.pager.adapter = tabsAdapter
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, binding.pager) { tab, position ->
            tab.text = fragmentTitles()[position]
        }.attach()

        viewModel.author.observe(viewLifecycleOwner, Observer { author ->
            binding.ptAuthorImageIv.loadImg(author?.authorImgLink)
            binding.ptAuthorNameTv.fadeInText(author?.name)
            binding.ptAuthorDateTv.fadeInText(author?.life)
        })

        binding.ptToolbar.setNavigationOnClickListener {
            activity?.finish()
        }

        binding.ptAppbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val progress = -verticalOffset / (appBarLayout?.totalScrollRange?.toFloat() ?: 0f)
            binding.motion.progress = progress
            binding.motion.translationY = -verticalOffset.toFloat()

            val result = ColorUtils.blendARGB(getThemeColor(R.attr.colorPrimary), getThemeColor(R.attr.colorPrimaryVariant), progress)

            binding.ptAppbar.setBackgroundColor(result)
        })

        binding.motion.apply {
            doOnLayout {
                val toolbarHeight = binding.ptToolbar.measuredHeight
                val tabsHeight = binding.tabLayout.measuredHeight

                val requiredChildHeight = toolbarHeight + tabsHeight + binding.ptAuthorImageHolderIv.measuredHeight + context.dpToPx(16)
                val minimumChildHeight = toolbarHeight + tabsHeight

                updateLayoutParams<ViewGroup.LayoutParams> { height = requiredChildHeight }
                minimumHeight = minimumChildHeight
            }
        }
    }

    private fun fragmentTitles(): List<String> {
        return listOf(
            getString(R.string.pt_bio),
            getString(R.string.pt_books)
        )
    }

    private fun fragments(): List<Fragment> {
        return listOf(AuthorBioFragment().apply {
            arguments = Bundle().apply {
                putString("id", viewModel.authorId)
            }
        }, AuthorBooksFragment().apply {
            arguments = Bundle().apply {
                putString("id", viewModel.authorId)
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
