    private void openDetailFragmentFromCommentReplies(
            @NonNull final FragmentManager fm,
            final boolean popBackStack
    ) {
        // obtain the name of the fragment under the replies fragment that's going to be popped
        @Nullable final String fragmentUnderEntryName;
        if (fm.getBackStackEntryCount() < 2) {
            fragmentUnderEntryName = null;
        } else {
            fragmentUnderEntryName = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 2)
                    .getName();
        }

        // the root comment is the comment for which the user opened the replies page
        @Nullable final CommentRepliesFragment repliesFragment =
                (CommentRepliesFragment) fm.findFragmentByTag(CommentRepliesFragment.TAG);
        @Nullable final CommentsInfoItem rootComment =
                repliesFragment == null ? null : repliesFragment.getCommentsInfoItem();

        // sometimes this function pops the backstack, other times it's handled by the system
        if (popBackStack) {
            fm.popBackStackImmediate();
        }

        // only expand the bottom sheet back if there are no more nested comment replies fragments
        // stacked under the one that is currently being popped
        if (CommentRepliesFragment.TAG.equals(fragmentUnderEntryName)) {
            return;
        }

        final BottomSheetBehavior<FragmentContainerView> behavior = BottomSheetBehavior
                .from(mainBinding.fragmentPlayerHolder);
        // do not return to the comment if the details fragment was closed
        if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            return;
        }

        // scroll to the root comment once the bottom sheet expansion animation is finished
        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull final View bottomSheet,
                                       final int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    final Fragment detailFragment = fm.findFragmentById(
                            R.id.fragment_player_holder);
                    if (detailFragment instanceof VideoDetailFragment && rootComment != null) {
                        // should always be the case
                        ((VideoDetailFragment) detailFragment).scrollToComment(rootComment);
                    }
                    behavior.removeBottomSheetCallback(this);
                }
            }

            @Override
            public void onSlide(@NonNull final View bottomSheet, final float slideOffset) {
                // not needed, listener is removed once the sheet is expanded
            }
        });

        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
