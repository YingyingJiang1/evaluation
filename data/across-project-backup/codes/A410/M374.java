    @Override
    public boolean onMenuItemClick(@NonNull final MenuItem menuItem) {
        if (DEBUG) {
            Log.d(TAG, "onMenuItemClick() called with: "
                    + "menuItem = [" + menuItem + "], "
                    + "menuItem.getItemId = [" + menuItem.getItemId() + "]");
        }

        if (menuItem.getGroupId() == POPUP_MENU_ID_QUALITY) {
            onQualityItemClick(menuItem);
            return true;
        } else if (menuItem.getGroupId() == POPUP_MENU_ID_AUDIO_TRACK) {
            onAudioTrackItemClick(menuItem);
            return true;
        } else if (menuItem.getGroupId() == POPUP_MENU_ID_PLAYBACK_SPEED) {
            final int speedIndex = menuItem.getItemId();
            final float speed = PLAYBACK_SPEEDS[speedIndex];

            player.setPlaybackSpeed(speed);
            binding.playbackSpeed.setText(formatSpeed(speed));
        }

        return false;
    }
