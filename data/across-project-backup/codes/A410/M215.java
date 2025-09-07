    private void initUIsForCurrentPlayerType() {
        if ((UIs.get(MainPlayerUi.class).isPresent() && playerType == PlayerType.MAIN)
                || (UIs.get(PopupPlayerUi.class).isPresent() && playerType == PlayerType.POPUP)) {
            // correct UI already in place
            return;
        }

        // try to reuse binding if possible
        final PlayerBinding binding = UIs.get(VideoPlayerUi.class).map(VideoPlayerUi::getBinding)
                .orElseGet(() -> {
                    if (playerType == PlayerType.AUDIO) {
                        return null;
                    } else {
                        return PlayerBinding.inflate(LayoutInflater.from(context));
                    }
                });

        switch (playerType) {
            case MAIN:
                UIs.destroyAll(PopupPlayerUi.class);
                UIs.addAndPrepare(new MainPlayerUi(this, binding));
                break;
            case POPUP:
                UIs.destroyAll(MainPlayerUi.class);
                UIs.addAndPrepare(new PopupPlayerUi(this, binding));
                break;
            case AUDIO:
                UIs.destroyAll(VideoPlayerUi.class);
                break;
        }
    }
