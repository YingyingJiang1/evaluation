    private boolean waitForJob(Job job, int timeout) {
        long startTime = System.currentTimeMillis();
        while (true) {
            switch (job.status()) {
                case STOPPED:
                case TERMINATED:
                    return true;
            }
            if (System.currentTimeMillis() - startTime > timeout) {
                return false;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }
