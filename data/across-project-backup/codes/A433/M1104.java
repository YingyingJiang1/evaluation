    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (mStartButton != null && mPauseButton != null) {
            checkMasterButtonsVisibility();
        }

        switch (msg.what) {
            case DownloadManagerService.MESSAGE_ERROR:
            case DownloadManagerService.MESSAGE_FINISHED:
            case DownloadManagerService.MESSAGE_DELETED:
            case DownloadManagerService.MESSAGE_PAUSED:
                break;
            default:
                return false;
        }

        ViewHolderItem h = getViewHolder(msg.obj);
        if (h == null) return false;

        switch (msg.what) {
            case DownloadManagerService.MESSAGE_FINISHED:
            case DownloadManagerService.MESSAGE_DELETED:
                // DownloadManager should mark the download as finished
                applyChanges();
                return true;
        }

        updateProgress(h);
        return true;
    }
