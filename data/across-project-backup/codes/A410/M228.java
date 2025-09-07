    private Target getCurrentThumbnailTarget() {
        // a Picasso target is just a listener for thumbnail loading events
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, final Picasso.LoadedFrom from) {
                if (DEBUG) {
                    Log.d(TAG, "Thumbnail - onBitmapLoaded() called with: bitmap = [" + bitmap
                            + " -> " + bitmap.getWidth() + "x" + bitmap.getHeight() + "], from = ["
                            + from + "]");
                }
                // there is a new thumbnail, so e.g. the end screen thumbnail needs to change, too.
                onThumbnailLoaded(bitmap);
            }

            @Override
            public void onBitmapFailed(final Exception e, final Drawable errorDrawable) {
                Log.e(TAG, "Thumbnail - onBitmapFailed() called", e);
                // there is a new thumbnail, so e.g. the end screen thumbnail needs to change, too.
                onThumbnailLoaded(null);
            }

            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                if (DEBUG) {
                    Log.d(TAG, "Thumbnail - onPrepareLoad() called");
                }
            }
        };
    }
