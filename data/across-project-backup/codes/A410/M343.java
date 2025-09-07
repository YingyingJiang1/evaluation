    private void adjustSeekbarPreviewContainer() {
        try {
            // Should only be required when an error occurred before
            // and the layout was positioned in the center
            binding.bottomSeekbarPreviewLayout.setGravity(Gravity.NO_GRAVITY);

            // Calculate the current left position of seekbar progress in px
            // More info: https://stackoverflow.com/q/20493577
            final int currentSeekbarLeft =
                    binding.playbackSeekBar.getLeft()
                            + binding.playbackSeekBar.getPaddingLeft()
                            + binding.playbackSeekBar.getThumb().getBounds().left;

            // Calculate the (unchecked) left position of the container
            final int uncheckedContainerLeft =
                    currentSeekbarLeft - (binding.seekbarPreviewContainer.getWidth() / 2);

            // Fix the position so it's within the boundaries
            final int checkedContainerLeft = MathUtils.clamp(uncheckedContainerLeft,
                    0, binding.playbackWindowRoot.getWidth()
                            - binding.seekbarPreviewContainer.getWidth());

            // See also: https://stackoverflow.com/a/23249734
            final LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            binding.seekbarPreviewContainer.getLayoutParams());
            params.setMarginStart(checkedContainerLeft);
            binding.seekbarPreviewContainer.setLayoutParams(params);
        } catch (final Exception ex) {
            Log.e(TAG, "Failed to adjust seekbarPreviewContainer", ex);
            // Fallback - position in the middle
            binding.bottomSeekbarPreviewLayout.setGravity(Gravity.CENTER);
        }
    }
