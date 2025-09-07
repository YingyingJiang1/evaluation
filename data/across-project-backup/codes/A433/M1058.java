    private void startMission(Intent intent) {
        String[] urls = intent.getStringArrayExtra(EXTRA_URLS);
        Uri path = IntentCompat.getParcelableExtra(intent, EXTRA_PATH, Uri.class);
        Uri parentPath = IntentCompat.getParcelableExtra(intent, EXTRA_PARENT_PATH, Uri.class);
        int threads = intent.getIntExtra(EXTRA_THREADS, 1);
        char kind = intent.getCharExtra(EXTRA_KIND, '?');
        String psName = intent.getStringExtra(EXTRA_POSTPROCESSING_NAME);
        String[] psArgs = intent.getStringArrayExtra(EXTRA_POSTPROCESSING_ARGS);
        String source = intent.getStringExtra(EXTRA_SOURCE);
        long nearLength = intent.getLongExtra(EXTRA_NEAR_LENGTH, 0);
        String tag = intent.getStringExtra(EXTRA_STORAGE_TAG);
        final var recovery = IntentCompat.getParcelableArrayListExtra(intent, EXTRA_RECOVERY_INFO,
                MissionRecoveryInfo.class);
        Objects.requireNonNull(recovery);

        StoredFileHelper storage;
        try {
            storage = new StoredFileHelper(this, parentPath, path, tag);
        } catch (IOException e) {
            throw new RuntimeException(e);// this never should happen
        }

        Postprocessing ps;
        if (psName == null)
            ps = null;
        else
            ps = Postprocessing.getAlgorithm(psName, psArgs);

        final DownloadMission mission = new DownloadMission(urls, storage, kind, ps);
        mission.threadCount = threads;
        mission.source = source;
        mission.nearLength = nearLength;
        mission.recoveryInfo = recovery.toArray(new MissionRecoveryInfo[0]);

        if (ps != null)
            ps.setTemporalDir(DownloadManager.pickAvailableTemporalDir(this));

        handleConnectivityState(true);// first check the actual network status

        mManager.startMission(mission);
    }
