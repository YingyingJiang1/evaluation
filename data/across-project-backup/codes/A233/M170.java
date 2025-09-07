    private void shutdownSchedulers() {
        log.info("Shutting down job queue");
        shuttingDown = true;

        // Complete any futures that are still waiting
        jobMap.forEach(
                (id, job) -> {
                    if (!job.future.isDone()) {
                        job.future.completeExceptionally(
                                new RuntimeException("Server shutting down, job cancelled"));
                    }
                });

        // Shutdown schedulers and wait for termination
        try {
            scheduler.shutdown();
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }

            jobExecutor.shutdown();
            if (!jobExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                jobExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            scheduler.shutdownNow();
            jobExecutor.shutdownNow();
        }

        log.info(
                "Job queue shutdown complete. Stats: total={}, rejected={}",
                totalQueuedJobs,
                rejectedJobs);
    }
