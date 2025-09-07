    public static String getActionName(@NonNull final Context context, @Action final int action) {
        switch (action) {
            case PREVIOUS:
                return context.getString(com.google.android.exoplayer2.ui.R.string
                        .exo_controls_previous_description);
            case NEXT:
                return context.getString(com.google.android.exoplayer2.ui.R.string
                        .exo_controls_next_description);
            case REWIND:
                return context.getString(com.google.android.exoplayer2.ui.R.string
                        .exo_controls_rewind_description);
            case FORWARD:
                return context.getString(com.google.android.exoplayer2.ui.R.string
                        .exo_controls_fastforward_description);
            case SMART_REWIND_PREVIOUS:
                return Localization.concatenateStrings(
                        context.getString(com.google.android.exoplayer2.ui.R.string
                                .exo_controls_rewind_description),
                        context.getString(com.google.android.exoplayer2.ui.R.string
                                .exo_controls_previous_description));
            case SMART_FORWARD_NEXT:
                return Localization.concatenateStrings(
                        context.getString(com.google.android.exoplayer2.ui.R.string
                                .exo_controls_fastforward_description),
                        context.getString(com.google.android.exoplayer2.ui.R.string
                                .exo_controls_next_description));
            case PLAY_PAUSE:
                return Localization.concatenateStrings(
                        context.getString(com.google.android.exoplayer2.ui.R.string
                                .exo_controls_play_description),
                        context.getString(com.google.android.exoplayer2.ui.R.string
                                .exo_controls_pause_description));
            case PLAY_PAUSE_BUFFERING:
                return Localization.concatenateStrings(
                        context.getString(com.google.android.exoplayer2.ui.R.string
                                .exo_controls_play_description),
                        context.getString(com.google.android.exoplayer2.ui.R.string
                                .exo_controls_pause_description),
                        context.getString(R.string.notification_action_buffering));
            case REPEAT:
                return context.getString(R.string.notification_action_repeat);
            case SHUFFLE:
                return context.getString(R.string.notification_action_shuffle);
            case CLOSE:
                return context.getString(R.string.close);
            case NOTHING: default:
                return context.getString(R.string.notification_action_nothing);
        }
    }
