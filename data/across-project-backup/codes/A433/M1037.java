    public void pause() {
        if (!running) return;

        if (isPsRunning()) {
            if (DEBUG) {
                Log.w(TAG, "pause during post-processing is not applicable.");
            }
            return;
        }

        running = false;
        notify(DownloadManagerService.MESSAGE_PAUSED);

        if (init != null && init.isAlive()) {
            // NOTE: if start() method is running ¡will no have effect!
            init.interrupt();
            synchronized (LOCK) {
                resetState(false, true, ERROR_NOTHING);
            }
            return;
        }

        if (DEBUG && unknownLength) {
            Log.w(TAG, "pausing a download that can not be resumed (range requests not allowed by the server).");
        }

        init = null;
        pauseThreads();
    }
