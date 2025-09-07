    @Override
    protected void setupElementsVisibility() {
        binding.fullScreenButton.setVisibility(View.VISIBLE);
        binding.screenRotationButton.setVisibility(View.GONE);
        binding.resizeTextView.setVisibility(View.GONE);
        binding.getRoot().findViewById(R.id.metadataView).setVisibility(View.GONE);
        binding.queueButton.setVisibility(View.GONE);
        binding.segmentsButton.setVisibility(View.GONE);
        binding.moreOptionsButton.setVisibility(View.GONE);
        binding.topControls.setOrientation(LinearLayout.HORIZONTAL);
        binding.primaryControls.getLayoutParams().width = WRAP_CONTENT;
        binding.secondaryControls.setAlpha(1.0f);
        binding.secondaryControls.setVisibility(View.VISIBLE);
        binding.secondaryControls.setTranslationY(0);
        binding.share.setVisibility(View.GONE);
        binding.playWithKodi.setVisibility(View.GONE);
        binding.openInBrowser.setVisibility(View.GONE);
        binding.switchMute.setVisibility(View.GONE);
        binding.playerCloseButton.setVisibility(View.GONE);
        binding.topControls.bringToFront();
        binding.topControls.setClickable(false);
        binding.topControls.setFocusable(false);
        binding.bottomControls.bringToFront();
        super.setupElementsVisibility();
    }
