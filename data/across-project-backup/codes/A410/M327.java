    protected void deinitListeners() {
        binding.qualityTextView.setOnClickListener(null);
        binding.audioTrackTextView.setOnClickListener(null);
        binding.playbackSpeed.setOnClickListener(null);
        binding.playbackSeekBar.setOnSeekBarChangeListener(null);
        binding.captionTextView.setOnClickListener(null);
        binding.resizeTextView.setOnClickListener(null);
        binding.playbackLiveSync.setOnClickListener(null);

        binding.getRoot().setOnTouchListener(null);
        playerGestureListener = null;
        gestureDetector = null;

        binding.repeatButton.setOnClickListener(null);
        binding.shuffleButton.setOnClickListener(null);

        binding.playPauseButton.setOnClickListener(null);
        binding.playPreviousButton.setOnClickListener(null);
        binding.playNextButton.setOnClickListener(null);

        binding.moreOptionsButton.setOnClickListener(null);
        binding.moreOptionsButton.setOnLongClickListener(null);
        binding.share.setOnClickListener(null);
        binding.share.setOnLongClickListener(null);
        binding.fullScreenButton.setOnClickListener(null);
        binding.screenRotationButton.setOnClickListener(null);
        binding.playWithKodi.setOnClickListener(null);
        binding.openInBrowser.setOnClickListener(null);
        binding.playerCloseButton.setOnClickListener(null);
        binding.switchMute.setOnClickListener(null);

        ViewCompat.setOnApplyWindowInsetsListener(binding.itemsListPanel, null);

        binding.playbackControlRoot.removeOnLayoutChangeListener(onLayoutChangeListener);
    }
