    private void buildQualityMenu() {
        if (qualityPopupMenu == null) {
            return;
        }
        qualityPopupMenu.getMenu().removeGroup(POPUP_MENU_ID_QUALITY);

        final List<VideoStream> availableStreams = Optional.ofNullable(player.getCurrentMetadata())
                .flatMap(MediaItemTag::getMaybeQuality)
                .map(MediaItemTag.Quality::getSortedVideoStreams)
                .orElse(null);
        if (availableStreams == null) {
            return;
        }

        for (int i = 0; i < availableStreams.size(); i++) {
            final VideoStream videoStream = availableStreams.get(i);
            qualityPopupMenu.getMenu().add(POPUP_MENU_ID_QUALITY, i, Menu.NONE, MediaFormat
                    .getNameById(videoStream.getFormatId()) + " " + videoStream.getResolution());
        }
        qualityPopupMenu.setOnMenuItemClickListener(this);
        qualityPopupMenu.setOnDismissListener(this);

        player.getSelectedVideoStream()
                .ifPresent(s -> binding.qualityTextView.setText(s.getResolution()));
    }
