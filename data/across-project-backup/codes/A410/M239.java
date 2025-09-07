    private void updatePlaybackState(final boolean playWhenReady, final int playbackState) {
        if (DEBUG) {
            Log.d(TAG, "ExoPlayer - updatePlaybackState() called with: "
                    + "playWhenReady = [" + playWhenReady + "], "
                    + "playbackState = [" + playbackState + "]");
        }

        if (currentState == STATE_PAUSED_SEEK) {
            if (DEBUG) {
                Log.d(TAG, "updatePlaybackState() is currently blocked");
            }
            return;
        }

        switch (playbackState) {
            case com.google.android.exoplayer2.Player.STATE_IDLE: // 1
                isPrepared = false;
                break;
            case com.google.android.exoplayer2.Player.STATE_BUFFERING: // 2
                if (isPrepared) {
                    changeState(STATE_BUFFERING);
                }
                break;
            case com.google.android.exoplayer2.Player.STATE_READY: //3
                if (!isPrepared) {
                    isPrepared = true;
                    onPrepared(playWhenReady);
                }
                changeState(playWhenReady ? STATE_PLAYING : STATE_PAUSED);
                break;
            case com.google.android.exoplayer2.Player.STATE_ENDED: // 4
                changeState(STATE_COMPLETED);
                saveStreamProgressStateCompleted();
                isPrepared = false;
                break;
        }
    }
