package cz.legat.prectito.ui.main.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import cz.legat.prectito.R
import cz.legat.prectito.databinding.PtDetailTabsFragmentBinding
import cz.legat.prectito.ui.main.BindingFragment

abstract class BaseTabsFragment : BindingFragment<PtDetailTabsFragmentBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PtDetailTabsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.ptDetailPager.adapter = DetailAdapter(this)
        TabLayoutMediator(binding.ptDetailTabs, binding.ptDetailPager) { tab, position ->
            tab.text = tabTitles()[position]
        }.attach()
    }

    inner class DetailAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = fragments().size

        override fun createFragment(position: Int): Fragment {
            return fragments()[position]
        }
    }

    abstract fun fragments(): List<Fragment>
    abstract fun tabTitles(): List<String>
}