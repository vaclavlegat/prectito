package cz.legat.prectito.ui.main.authors.detail

import androidx.fragment.app.Fragment
import cz.legat.prectito.BaseTabsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorDetailFragment : BaseTabsFragment() {

    override fun fragments(): List<Fragment> {
        return listOf(AuthorBioFragment.newInstance(id!!), AuthorBooksFragment.newInstance(id!!))
    }

    override fun tabTitles(): List<String> {
        return listOf("Info", "Books")
    }
}
