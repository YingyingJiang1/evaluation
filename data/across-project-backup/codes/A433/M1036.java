    public void start() {
        if (running || isFinished() || urls.length < 1) return;

        // ensure that the previous state is completely paused.
        joinForThreads(10000);

        running = true;
        errCode = ERROR_NOTHING;

        if (hasInvalidStorage()) {
            notifyError(ERROR_FILE_CREATION, null);
            return;
        }

        if (current >= urls.length) {
            notifyFinished();
            return;
        }

        notify(DownloadManagerService.MESSAGE_RUNNING);

        if (urls[current] == null) {
            doRecover(ERROR_RESOURCE_GONE);
            return;
        }

        if (blocks == null) {
            initializer();
            return;
        }

        init = null;
        finishCount = 0;
        blockAcquired = new boolean[blocks.length];

        if (blocks.length < 1) {
            threads = new Thread[]{runAsync(1, new DownloadRunnableFallback(this))};
        } else {
            int remainingBlocks = 0;
            for (int block : blocks) if (block >= 0) remainingBlocks++;

            if (remainingBlocks < 1) {
                notifyFinished();
                return;
            }

            threads = new Thread[Math.min(threadCount, remainingBlocks)];

            for (int i = 0; i < threads.length; i++) {
                threads[i] = runAsync(i + 1, new DownloadRunnable(this, i));
            }
        }
    }
