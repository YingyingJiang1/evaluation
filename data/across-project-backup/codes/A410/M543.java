    private synchronized NotificationCompat.Builder createNotification() {
        if (DEBUG) {
            Log.d(TAG, "createNotification()");
        }
        notificationManager = NotificationManagerCompat.from(player.getContext());
        final NotificationCompat.Builder builder =
                new NotificationCompat.Builder(player.getContext(),
                player.getContext().getString(R.string.notification_channel_id));
        final MediaStyle mediaStyle = new MediaStyle();

        // setup media style (compact notification slots and media session)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            // notification actions are ignored on Android 13+, and are replaced by code in
            // MediaSessionPlayerUi
            final int[] compactSlots = initializeNotificationSlots();
            mediaStyle.setShowActionsInCompactView(compactSlots);
        }
        player.UIs()
                .get(MediaSessionPlayerUi.class)
                .flatMap(MediaSessionPlayerUi::getSessionToken)
                .ifPresent(mediaStyle::setMediaSession);

        // setup notification builder
        builder.setStyle(mediaStyle)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
                .setShowWhen(false)
                .setSmallIcon(R.drawable.ic_newpipe_triangle_white)
                .setColor(ContextCompat.getColor(player.getContext(),
                        R.color.dark_background_color))
                .setColorized(player.getPrefs().getBoolean(
                        player.getContext().getString(R.string.notification_colorize_key), true))
                .setDeleteIntent(PendingIntentCompat.getBroadcast(player.getContext(),
                        NOTIFICATION_ID, new Intent(ACTION_CLOSE), FLAG_UPDATE_CURRENT, false));

        // set the initial value for the video thumbnail, updatable with updateNotificationThumbnail
        setLargeIcon(builder);

        return builder;
    }
