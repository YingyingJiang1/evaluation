    public void cleanupOldJobs() {
        LocalDateTime expiryThreshold =
                LocalDateTime.now().minus(jobResultExpiryMinutes, ChronoUnit.MINUTES);
        int removedCount = 0;

        try {
            for (Map.Entry<String, JobResult> entry : jobResults.entrySet()) {
                JobResult result = entry.getValue();

                // Remove completed jobs that are older than the expiry threshold
                if (result.isComplete()
                        && result.getCompletedAt() != null
                        && result.getCompletedAt().isBefore(expiryThreshold)) {

                    // Clean up file results
                    cleanupJobFiles(result, entry.getKey());

                    // Remove the job result
                    jobResults.remove(entry.getKey());
                    removedCount++;
                }
            }

            if (removedCount > 0) {
                log.info("Cleaned up {} expired job results", removedCount);
            }
        } catch (Exception e) {
            log.error("Error during job cleanup: {}", e.getMessage(), e);
        }
    }
