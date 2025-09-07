    private PlayQueueItemBuilder.OnSelectedListener getOnSelectedListener() {
        return new PlayQueueItemBuilder.OnSelectedListener() {
            @Override
            public void selected(final PlayQueueItem item, final View view) {
                player.selectQueueItem(item);
            }

            @Override
            public void held(final PlayQueueItem item, final View view) {
                @Nullable final PlayQueue playQueue = player.getPlayQueue();
                @Nullable final AppCompatActivity parentActivity = getParentActivity().orElse(null);
                if (playQueue != null && parentActivity != null && playQueue.indexOf(item) != -1) {
                    openPopupMenu(player.getPlayQueue(), item, view, true,
                            parentActivity.getSupportFragmentManager(), context);
                }
            }

            @Override
            public void onStartDrag(final PlayQueueItemHolder viewHolder) {
                if (itemTouchHelper != null) {
                    itemTouchHelper.startDrag(viewHolder);
                }
            }
        };
    }
