    private void openMiniPlayerUponPlayerStarted() {
        if (getIntent().getSerializableExtra(Constants.KEY_LINK_TYPE)
                == StreamingService.LinkType.STREAM) {
            // handleIntent() already takes care of opening video detail fragment
            // due to an intent containing a STREAM link
            return;
        }

        if (PlayerHolder.getInstance().isPlayerOpen()) {
            // if the player is already open, no need for a broadcast receiver
            openMiniPlayerIfMissing();
        } else {
            // listen for player start intent being sent around
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(final Context context, final Intent intent) {
                    if (Objects.equals(intent.getAction(),
                            VideoDetailFragment.ACTION_PLAYER_STARTED)
                            && PlayerHolder.getInstance().isPlayerOpen()) {
                        openMiniPlayerIfMissing();
                        // At this point the player is added 100%, we can unregister. Other actions
                        // are useless since the fragment will not be removed after that.
                        unregisterReceiver(broadcastReceiver);
                        broadcastReceiver = null;
                    }
                }
            };
            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(VideoDetailFragment.ACTION_PLAYER_STARTED);
            registerReceiver(broadcastReceiver, intentFilter);

            // If the PlayerHolder is not bound yet, but the service is running, try to bind to it.
            // Once the connection is established, the ACTION_PLAYER_STARTED will be sent.
            PlayerHolder.getInstance().tryBindIfNeeded(this);
        }
    }
