package cz.legat.authors.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cz.legat.authors.ui.BaseTabsFragment
import cz.legat.core.extensions.AppBarOffsetOffsetChangedListener
import cz.legat.core.extensions.OnAppBarOffsetChangedListener
import cz.legat.core.extensions.fadeInText
import cz.legat.core.extensions.loadImg
import cz.legat.core.extensions.loadWithBackground
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorDetailFragment : BaseTabsFragment() {

    private val viewModel: AuthorDetailViewModel by viewModels()

    override fun fragments(): List<Fragment> {
        return listOf(AuthorBioFragment.newInstance(id!!), AuthorBooksFragment.newInstance(id!!))
    }

    override fun tabTitles(): List<String> {
        return listOf("Info", "Books")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.author.observe(viewLifecycleOwner, Observer {
            binding.collapsing.fadeInText(it?.name)
            binding.ptAuthorImageIv.loadImg(it?.authorImgLink)
            binding.ptAuthorImageIv.loadWithBackground(it?.authorImgLink, binding.ptImageBg)

            binding.appbarLayout.addOnOffsetChangedListener(AppBarOffsetOffsetChangedListener(object : OnAppBarOffsetChangedListener {
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
