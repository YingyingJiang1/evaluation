    private void animatePlayButtons(final boolean show, final long duration) {
        animate(binding.playPauseButton, show, duration, AnimationType.SCALE_AND_ALPHA);

        @Nullable final PlayQueue playQueue = player.getPlayQueue();
        if (playQueue == null) {
            return;
        }

        if (!show || playQueue.getIndex() > 0) {
            animate(
                    binding.playPreviousButton,
                    show,
                    duration,
                    AnimationType.SCALE_AND_ALPHA);
        }
        if (!show || playQueue.getIndex() + 1 < playQueue.getStreams().size()) {
            animate(
                    binding.playNextButton,
                    show,
                    duration,
                    AnimationType.SCALE_AND_ALPHA);
        }
    }
