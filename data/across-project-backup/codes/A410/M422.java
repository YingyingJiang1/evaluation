    @Override
    protected void setupElementsVisibility() {
        super.setupElementsVisibility();

        closeItemsList();
        showHideKodiButton();
        binding.fullScreenButton.setVisibility(View.GONE);
        setupScreenRotationButton();
        binding.resizeTextView.setVisibility(View.VISIBLE);
        binding.getRoot().findViewById(R.id.metadataView).setVisibility(View.VISIBLE);
        binding.moreOptionsButton.setVisibility(View.VISIBLE);
        binding.topControls.setOrientation(LinearLayout.VERTICAL);
        binding.primaryControls.getLayoutParams().width = MATCH_PARENT;
        binding.secondaryControls.setVisibility(View.INVISIBLE);
        binding.moreOptionsButton.setImageDrawable(AppCompatResources.getDrawable(context,
                R.drawable.ic_expand_more));
        binding.share.setVisibility(View.VISIBLE);
        binding.openInBrowser.setVisibility(View.VISIBLE);
        binding.switchMute.setVisibility(View.VISIBLE);
        binding.playerCloseButton.setVisibility(isFullscreen ? View.GONE : View.VISIBLE);
        // Top controls have a large minHeight which is allows to drag the player
        // down in fullscreen mode (just larger area to make easy to locate by finger)
        binding.topControls.setClickable(true);
        binding.topControls.setFocusable(true);

        binding.titleTextView.setVisibility(isFullscreen ? View.VISIBLE : View.GONE);
        binding.channelTextView.setVisibility(isFullscreen ? View.VISIBLE : View.GONE);
    }
