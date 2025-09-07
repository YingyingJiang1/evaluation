    private void updateQueueTime(final int currentTime) {
        @Nullable final PlayQueue playQueue = player.getPlayQueue();
        if (playQueue == null) {
            return;
        }

        final int currentStream = playQueue.getIndex();
        int before = 0;
        int after = 0;

        final List<PlayQueueItem> streams = playQueue.getStreams();
        final int nStreams = streams.size();

        for (int i = 0; i < nStreams; i++) {
            if (i < currentStream) {
                before += streams.get(i).getDuration();
            } else {
                after += streams.get(i).getDuration();
            }
        }

        before *= 1000;
        after *= 1000;

        binding.itemsListHeaderDuration.setText(
                String.format("%s/%s",
                        getTimeString(currentTime + before),
                        getTimeString(before + after)
                ));
    }
