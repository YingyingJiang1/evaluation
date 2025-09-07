    @Override
    public void onUpdateProgress(final int currentProgress,
                                 final int duration,
                                 final int bufferPercent) {

        if (duration != binding.playbackSeekBar.getMax()) {
            setVideoDurationToControls(duration);
        }
        if (player.getCurrentState() != STATE_PAUSED) {
            updatePlayBackElementsCurrentDuration(currentProgress);
        }
        if (player.isLoading() || bufferPercent > 90) {
            binding.playbackSeekBar.setSecondaryProgress(
                    (int) (binding.playbackSeekBar.getMax() * ((float) bufferPercent / 100)));
        }
        if (DEBUG && bufferPercent % 20 == 0) { //Limit log
            Log.d(TAG, "notifyProgressUpdateToListeners() called with: "
                    + "isVisible = " + isControlsVisible() + ", "
                    + "currentProgress = [" + currentProgress + "], "
                    + "duration = [" + duration + "], bufferPercent = [" + bufferPercent + "]");
        }
        binding.playbackLiveSync.setClickable(!player.isLiveEdge());
    }
