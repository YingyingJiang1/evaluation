    private void updateTabLayoutPosition() {
        final ScrollableTabLayout tabLayout = binding.mainTabLayout;
        final ViewPager viewPager = binding.pager;
        final boolean bottom = mainTabsPositionBottom;

        // change layout params to make the tab layout appear either at the top or at the bottom
        final var tabParams = (RelativeLayout.LayoutParams) tabLayout.getLayoutParams();
        final var pagerParams = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();

        tabParams.removeRule(bottom ? ALIGN_PARENT_TOP : ALIGN_PARENT_BOTTOM);
        tabParams.addRule(bottom ? ALIGN_PARENT_BOTTOM : ALIGN_PARENT_TOP);
        pagerParams.removeRule(bottom ? BELOW : ABOVE);
        pagerParams.addRule(bottom ? ABOVE : BELOW, R.id.main_tab_layout);
        tabLayout.setSelectedTabIndicatorGravity(
                bottom ? INDICATOR_GRAVITY_TOP : INDICATOR_GRAVITY_BOTTOM);

        tabLayout.setLayoutParams(tabParams);
        viewPager.setLayoutParams(pagerParams);

        // change the background and icon color of the tab layout:
        // service-colored at the top, app-background-colored at the bottom
        tabLayout.setBackgroundColor(ThemeHelper.resolveColorFromAttr(requireContext(),
                bottom ? android.R.attr.windowBackground : R.attr.colorPrimary));

        @ColorInt final int iconColor = bottom
                ? ThemeHelper.resolveColorFromAttr(requireContext(), android.R.attr.colorAccent)
                : Color.WHITE;
        tabLayout.setTabRippleColor(ColorStateList.valueOf(iconColor).withAlpha(32));
        tabLayout.setTabIconTint(ColorStateList.valueOf(iconColor));
        tabLayout.setSelectedTabIndicatorColor(iconColor);
    }
