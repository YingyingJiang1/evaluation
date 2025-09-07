    public MediaDescriptionCompat getQueueMetadata(final int index) {
        if (player.getPlayQueue() == null) {
            return null;
        }
        final PlayQueueItem item = player.getPlayQueue().getItem(index);
        if (item == null) {
            return null;
        }

        final MediaDescriptionCompat.Builder descBuilder = new MediaDescriptionCompat.Builder()
                .setMediaId(String.valueOf(index))
                .setTitle(item.getTitle())
                .setSubtitle(item.getUploader());

        // set additional metadata for A2DP/AVRCP (Audio/Video Bluetooth profiles)
        final Bundle additionalMetadata = new Bundle();
        additionalMetadata.putString(MediaMetadataCompat.METADATA_KEY_TITLE, item.getTitle());
        additionalMetadata.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, item.getUploader());
        additionalMetadata
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, item.getDuration() * 1000);
        additionalMetadata.putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, index + 1L);
        additionalMetadata
                .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, player.getPlayQueue().size());
        descBuilder.setExtras(additionalMetadata);

        try {
            descBuilder.setIconUri(Uri.parse(
                    ImageStrategy.choosePreferredImage(item.getThumbnails())));
        } catch (final Throwable e) {
            // no thumbnail available at all, or the user disabled image loading,
            // or the obtained url is not a valid `Uri`
        }

        return descBuilder.build();
    }
