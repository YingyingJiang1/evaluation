    private void updateSubscribeButton(final boolean isSubscribed) {
        if (DEBUG) {
            Log.d(TAG, "updateSubscribeButton() called with: "
                    + "isSubscribed = [" + isSubscribed + "]");
        }

        final boolean isButtonVisible = binding.channelSubscribeButton.getVisibility()
                == View.VISIBLE;
        final int backgroundDuration = isButtonVisible ? 300 : 0;
        final int textDuration = isButtonVisible ? 200 : 0;

        final int subscribedBackground = ContextCompat
                .getColor(activity, R.color.subscribed_background_color);
        final int subscribedText = ContextCompat.getColor(activity, R.color.subscribed_text_color);
        final int subscribeBackground = ColorUtils.blendARGB(ThemeHelper
                .resolveColorFromAttr(activity, R.attr.colorPrimary), subscribedBackground, 0.35f);
        final int subscribeText = ContextCompat.getColor(activity, R.color.subscribe_text_color);

        if (isSubscribed) {
            binding.channelSubscribeButton.setText(R.string.subscribed_button_title);
            animateBackgroundColor(binding.channelSubscribeButton, backgroundDuration,
                    subscribeBackground, subscribedBackground);
            animateTextColor(binding.channelSubscribeButton, textDuration, subscribeText,
                    subscribedText);
        } else {
            binding.channelSubscribeButton.setText(R.string.subscribe_button_title);
            animateBackgroundColor(binding.channelSubscribeButton, backgroundDuration,
                    subscribedBackground, subscribeBackground);
            animateTextColor(binding.channelSubscribeButton, textDuration, subscribedText,
                    subscribeText);
        }

        animate(binding.channelSubscribeButton, true, 100, AnimationType.LIGHT_SCALE_AND_ALPHA);
    }
