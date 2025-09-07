    boolean runMissions() {
        synchronized (this) {
            if (mMissionsPending.size() < 1) return false;
            if (!canDownloadInCurrentNetwork()) return false;

            if (mPrefQueueLimit) {
                for (DownloadMission mission : mMissionsPending)
                    if (!mission.isFinished() && mission.running) return true;
            }

            boolean flag = false;
            for (DownloadMission mission : mMissionsPending) {
                if (mission.running || !mission.enqueued || mission.isFinished())
                    continue;

                resumeMission(mission);
                if (mission.errCode != DownloadMission.ERROR_NOTHING) continue;

                if (mPrefQueueLimit) return true;
                flag = true;
            }

            return flag;
        }
    }
