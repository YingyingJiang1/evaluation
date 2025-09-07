    public JobStats getJobStats() {
        int totalJobs = jobResults.size();
        int activeJobs = 0;
        int completedJobs = 0;
        int failedJobs = 0;
        int successfulJobs = 0;
        int fileResultJobs = 0;

        LocalDateTime oldestActiveJobTime = null;
        LocalDateTime newestActiveJobTime = null;
        long totalProcessingTimeMs = 0;

        for (JobResult result : jobResults.values()) {
            if (result.isComplete()) {
                completedJobs++;

                // Calculate processing time for completed jobs
                if (result.getCreatedAt() != null && result.getCompletedAt() != null) {
                    long processingTimeMs =
                            java.time.Duration.between(
                                            result.getCreatedAt(), result.getCompletedAt())
                                    .toMillis();
                    totalProcessingTimeMs += processingTimeMs;
                }

                if (result.getError() != null) {
                    failedJobs++;
                } else {
                    successfulJobs++;
                    if (result.hasFiles()) {
                        fileResultJobs++;
                    }
                }
            } else {
                activeJobs++;

                // Track oldest and newest active jobs
                if (result.getCreatedAt() != null) {
                    if (oldestActiveJobTime == null
                            || result.getCreatedAt().isBefore(oldestActiveJobTime)) {
                        oldestActiveJobTime = result.getCreatedAt();
                    }

                    if (newestActiveJobTime == null
                            || result.getCreatedAt().isAfter(newestActiveJobTime)) {
                        newestActiveJobTime = result.getCreatedAt();
                    }
                }
            }
        }

        // Calculate average processing time
        long averageProcessingTimeMs =
                completedJobs > 0 ? totalProcessingTimeMs / completedJobs : 0;

        return JobStats.builder()
                .totalJobs(totalJobs)
                .activeJobs(activeJobs)
                .completedJobs(completedJobs)
                .failedJobs(failedJobs)
                .successfulJobs(successfulJobs)
                .fileResultJobs(fileResultJobs)
                .oldestActiveJobTime(oldestActiveJobTime)
                .newestActiveJobTime(newestActiveJobTime)
                .averageProcessingTimeMs(averageProcessingTimeMs)
                .build();
    }
