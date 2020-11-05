package cz.legat.core.extensions

import com.google.android.material.appbar.AppBarLayout

private enum class CollapsingToolbarLayoutState {
    EXPANDED, COLLAPSED, INTERNEDIATE
}

interface OnAppBarOffsetChangedListener{
    fun onExpanded()
    fun onCollapsed()
    fun onIntermediate()
}

class AppBarOffsetOffsetChangedListener(val onAppBarOffsetChangedListener: OnAppBarOffsetChangedListener) : AppBarLayout.OnOffsetChangedListener {

    private var state = CollapsingToolbarLayoutState.EXPANDED

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        if (verticalOffset == 0) {
            if (state !== CollapsingToolbarLayoutState.EXPANDED) {
                state = CollapsingToolbarLayoutState.EXPANDED //Modify the status token to expand
                onAppBarOffsetChangedListener.onExpanded()
            }
        } else if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange) {
            if (state !== CollapsingToolbarLayoutState.COLLAPSED) {
                state = CollapsingToolbarLayoutState.COLLAPSED //Modified status marked as folded
                onAppBarOffsetChangedListener.onCollapsed()
            }
        } else {
            if (state !== CollapsingToolbarLayoutState.INTERNEDIATE) {
                if (state === CollapsingToolbarLayoutState.COLLAPSED) {

                }
                onAppBarOffsetChangedListener.onIntermediate()
                state = CollapsingToolbarLayoutState.INTERNEDIATE //Modify the status tag to the middle
            }
        }
    }
}