                    @NonNull
                    @Override
                    public FastSeekDirection getFastSeekDirection(
                            @NonNull final DisplayPortion portion
                    ) {
                        if (player.exoPlayerIsNull()) {
                            // Abort seeking
                            playerGestureListener.endMultiDoubleTap();
                            return FastSeekDirection.NONE;
                        }
                        if (portion == DisplayPortion.LEFT) {
                            // Check if it's possible to rewind
                            // Small puffer to eliminate infinite rewind seeking
                            if (player.getExoPlayer().getCurrentPosition() < 500L) {
                                return FastSeekDirection.NONE;
                            }
                            return FastSeekDirection.BACKWARD;
                        } else if (portion == DisplayPortion.RIGHT) {
                            // Check if it's possible to fast-forward
                            if (player.getCurrentState() == STATE_COMPLETED
                                    || player.getExoPlayer().getCurrentPosition()
                                    >= player.getExoPlayer().getDuration()) {
                                return FastSeekDirection.NONE;
                            }
                            return FastSeekDirection.FORWARD;
                        }
                        /* portion == DisplayPortion.MIDDLE */
                        return FastSeekDirection.NONE;
                    }
