    public void toggleFullscreen() {
        if (DEBUG) {
            Log.d(TAG, "toggleFullscreen() called");
        }
        final PlayerServiceEventListener fragmentListener = player.getFragmentListener()
                .orElse(null);
        if (fragmentListener == null || player.exoPlayerIsNull()) {
            return;
        }

        isFullscreen = !isFullscreen;
        if (isFullscreen) {
            // Android needs tens milliseconds to send new insets but a user is able to see
            // how controls changes it's position from `0` to `nav bar height` padding.
            // So just hide the controls to hide this visual inconsistency
            hideControls(0, 0);
        } else {
            // Apply window insets because Android will not do it when orientation changes
            // from landscape to portrait (open vertical video to reproduce)
            binding.playbackControlRoot.setPadding(0, 0, 0, 0);
        }
        fragmentListener.onFullscreenStateChanged(isFullscreen);

        binding.titleTextView.setVisibility(isFullscreen ? View.VISIBLE : View.GONE);
        binding.channelTextView.setVisibility(isFullscreen ? View.VISIBLE : View.GONE);
        binding.playerCloseButton.setVisibility(isFullscreen ? View.GONE : View.VISIBLE);
        setupScreenRotationButton();
    }
