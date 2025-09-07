    synchronized void notifyFinished() {
        if (current < urls.length) {
            if (++finishCount < threads.length) return;

            if (DEBUG) {
                Log.d(TAG, "onFinish: downloaded " + (current + 1) + "/" + urls.length);
            }

            current++;
            if (current < urls.length) {
                // prepare next sub-mission
                offsets[current] = offsets[current - 1] + length;
                initializer();
                return;
            }
        }

        if (psAlgorithm != null && psState == 0) {
            threads = new Thread[]{
                    runAsync(1, this::doPostprocessing)
            };
            return;
        }


        // this mission is fully finished

        unknownLength = false;
        enqueued = false;
        running = false;

        deleteThisFromFile();
        notify(DownloadManagerService.MESSAGE_FINISHED);
    }
