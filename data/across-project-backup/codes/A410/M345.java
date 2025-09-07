    @Override // seekbar listener
    public void onStopTrackingTouch(final SeekBar seekBar) {
        if (DEBUG) {
            Log.d(TAG, "onStopTrackingTouch() called with: seekBar = [" + seekBar + "]");
        }

        player.seekTo(seekBar.getProgress());
        if (player.getExoPlayer().getDuration() == seekBar.getProgress()) {
            player.getExoPlayer().play();
        }

        binding.playbackCurrentTime.setText(getTimeString(seekBar.getProgress()));
        animate(binding.currentDisplaySeek, false, 200, AnimationType.SCALE_AND_ALPHA);
        animate(binding.currentSeekbarPreviewThumbnail, false, 200, AnimationType.SCALE_AND_ALPHA);

        if (player.getCurrentState() == STATE_PAUSED_SEEK) {
            player.changeState(STATE_BUFFERING);
        }
        if (!player.isProgressLoopRunning()) {
            player.startProgressLoop();
        }

        showControlsThenHide();
    }
