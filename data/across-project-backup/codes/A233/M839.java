    public void start() throws IOException {
        // Check if the listener is already running
        if (process != null && process.isAlive()) {
            return;
        }

        // Start the listener process
        process = SystemCommand.runCommand(Runtime.getRuntime(), "unoconv --listener");
        lastActivityTime = System.currentTimeMillis();

        // Start a background thread to monitor the activity timeout
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(
                () -> {
                    while (true) {
                        long idleTime = System.currentTimeMillis() - lastActivityTime;
                        if (idleTime >= ACTIVITY_TIMEOUT) {
                            // If there has been no activity for too long, tear down the listener
                            process.destroy();
                            break;
                        }
                        try {
                            Thread.sleep(5000); // Check for inactivity every 5 seconds
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                });

        // Wait for the listener to start up
        long startTime = System.currentTimeMillis();
        long timeout = 30000; // Timeout after 30 seconds
        while (System.currentTimeMillis() - startTime < timeout) {
            if (isListenerRunning()) {

                lastActivityTime = System.currentTimeMillis();
                return;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("exception", e);
            } // Check every 1 second
        }
    }
