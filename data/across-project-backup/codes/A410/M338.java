    private void updateEndScreenThumbnail(@Nullable final Bitmap thumbnail) {
        if (thumbnail == null) {
            // remove end screen thumbnail
            binding.endScreen.setImageDrawable(null);
            return;
        }

        final float endScreenHeight = calculateMaxEndScreenThumbnailHeight(thumbnail);
        final Bitmap endScreenBitmap = BitmapCompat.createScaledBitmap(
                thumbnail,
                (int) (thumbnail.getWidth() / (thumbnail.getHeight() / endScreenHeight)),
                (int) endScreenHeight,
                null,
                true);

        if (DEBUG) {
            Log.d(TAG, "Thumbnail - onThumbnailLoaded() called with: "
                    + "currentThumbnail = [" + thumbnail + "], "
                    + thumbnail.getWidth() + "x" + thumbnail.getHeight()
                    + ", scaled end screen height = " + endScreenHeight
                    + ", scaled end screen width = " + endScreenBitmap.getWidth());
        }

        binding.endScreen.setImageBitmap(endScreenBitmap);
    }
