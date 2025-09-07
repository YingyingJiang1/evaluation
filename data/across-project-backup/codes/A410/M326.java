    private void initViews() {
        setupSubtitleView();

        binding.resizeTextView
                .setText(PlayerHelper.resizeTypeOf(context, binding.surfaceView.getResizeMode()));

        binding.playbackSeekBar.getThumb()
                .setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN));
        binding.playbackSeekBar.getProgressDrawable()
                .setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY));

        final ContextThemeWrapper themeWrapper = new ContextThemeWrapper(context,
                R.style.DarkPopupMenu);

        qualityPopupMenu = new PopupMenu(themeWrapper, binding.qualityTextView);
        audioTrackPopupMenu = new PopupMenu(themeWrapper, binding.audioTrackTextView);
        playbackSpeedPopupMenu = new PopupMenu(context, binding.playbackSpeed);
        captionPopupMenu = new PopupMenu(themeWrapper, binding.captionTextView);

        binding.progressBarLoadingPanel.getIndeterminateDrawable()
                .setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY));

        binding.titleTextView.setSelected(true);
        binding.channelTextView.setSelected(true);

        // Prevent hiding of bottom sheet via swipe inside queue
        binding.itemsList.setNestedScrollingEnabled(false);
    }
