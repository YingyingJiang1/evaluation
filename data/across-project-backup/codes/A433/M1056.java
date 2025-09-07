    private boolean handleMessage(@NonNull Message msg) {
        if (mHandler == null) return true;

        DownloadMission mission = (DownloadMission) msg.obj;

        switch (msg.what) {
            case MESSAGE_FINISHED:
                notifyMediaScanner(mission.storage.getUri());
                notifyFinishedDownload(mission.storage.getName());
                mManager.setFinished(mission);
                handleConnectivityState(false);
                updateForegroundState(mManager.runMissions());
                break;
            case MESSAGE_RUNNING:
                updateForegroundState(true);
                break;
            case MESSAGE_ERROR:
                notifyFailedDownload(mission);
                handleConnectivityState(false);
                updateForegroundState(mManager.runMissions());
                break;
            case MESSAGE_PAUSED:
                updateForegroundState(mManager.getRunningMissionsCount() > 0);
                break;
        }

        if (msg.what != MESSAGE_ERROR)
            mFailedDownloads.remove(mFailedDownloads.indexOfValue(mission));

        for (Callback observer : mEchoObservers)
            observer.handleMessage(msg);

        return true;
    }
