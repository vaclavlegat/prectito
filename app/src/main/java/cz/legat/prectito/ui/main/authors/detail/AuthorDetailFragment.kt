package cz.legat.prectito.ui.main.authors.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import cz.legat.core.extensions.fadeInText
import cz.legat.core.extensions.loadImg
import cz.legat.core.extensions.loadSingleBlurredImg
import cz.legat.prectito.BaseTabsFragment
import dagger.hilt.android.AndroidEntryPoint

private enum class CollapsingToolbarLayoutState {
    EXPANDED, COLLAPSED, INTERNEDIATE
}

@AndroidEntryPoint
class AuthorDetailFragment : BaseTabsFragment() {

    private var state: CollapsingToolbarLayoutState = CollapsingToolbarLayoutState.EXPANDED

    override fun fragments(): List<Fragment> {
        return listOf(AuthorBioFragment.newInstance(id!!), AuthorBooksFragment.newInstance(id!!))
    }

    override fun tabTitles(): List<String> {
        return listOf("Info", "Books")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.ptAuthorNameTv.fadeInText(name)
        binding.ptAuthorImageIv.loadImg(imgLink)
        binding.ptBookAuthorBgStartIv.loadSingleBlurredImg(imgLink)
        binding.ptBookAuthorBgMiddleIv.loadSingleBlurredImg(imgLink)
        binding.ptBookAuthorBgEndIv.loadSingleBlurredImg(imgLink)

        binding.appbarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                if (state !== CollapsingToolbarLayoutState.EXPANDED) {
                    state = CollapsingToolbarLayoutState.EXPANDED //Modify the status token to expand
                    binding.collapsing.title = "" //Set title to EXPANDED
                    //activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                }
            } else if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange) {
                if (state !== CollapsingToolbarLayoutState.COLLAPSED) {
                    binding.collapsing.fadeInText(name) //Set title not to display
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

    }
}
