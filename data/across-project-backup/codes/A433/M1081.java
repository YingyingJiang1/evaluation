    void handleConnectivityState(NetworkState currentStatus, boolean updateOnly) {
        if (currentStatus == mLastNetworkStatus) return;

        mLastNetworkStatus = currentStatus;
        if (currentStatus == NetworkState.Unavailable) return;

        if (!mSelfMissionsControl || updateOnly) {
            return;// don't touch anything without the user interaction
        }

        boolean isMetered = mPrefMeteredDownloads && mLastNetworkStatus == NetworkState.MeteredOperating;

        synchronized (this) {
            for (DownloadMission mission : mMissionsPending) {
                if (mission.isCorrupt() || mission.isPsRunning()) continue;

                if (mission.running && isMetered) {
                    mission.pause();
                } else if (!mission.running && !isMetered && mission.enqueued) {
                    mission.start();
                    if (mPrefQueueLimit) break;
                }
            }
        }
    }
