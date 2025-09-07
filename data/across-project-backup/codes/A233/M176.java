    private void processQueue() {
        // Jobs to execute after releasing the lock
        java.util.List<QueuedJob> jobsToExecute = new java.util.ArrayList<>();

        // First synchronized block: poll jobs from the queue and prepare them for execution
        synchronized (queueLock) {
            if (shuttingDown || jobQueue.isEmpty()) {
                return;
            }

            try {
                // Get current resource status
                ResourceMonitor.ResourceStatus status = resourceMonitor.getCurrentStatus().get();

                // Check if we should execute any jobs
                boolean canExecuteJobs = (status != ResourceMonitor.ResourceStatus.CRITICAL);

                if (!canExecuteJobs) {
                    // Under critical load, don't execute any jobs
                    log.debug("System under critical load, delaying job execution");
                    return;
                }

                // Get jobs from the queue, up to a limit based on resource availability
                int jobsToProcess =
                        Math.max(
                                1,
                                switch (status) {
                                    case OK -> 3;
                                    case WARNING -> 1;
                                    case CRITICAL -> 0;
                                });

                for (int i = 0; i < jobsToProcess && !jobQueue.isEmpty(); i++) {
                    QueuedJob job = jobQueue.poll();
                    if (job == null) break;

                    // Check if it's been waiting too long
                    long waitTimeMs = Instant.now().toEpochMilli() - job.queuedAt.toEpochMilli();
                    if (waitTimeMs > maxWaitTimeMs) {
                        log.warn(
                                "Job {} exceeded maximum wait time ({} ms), executing anyway",
                                job.jobId,
                                waitTimeMs);

                        // Add a specific status to the job context that can be tracked
                        // This will be visible in the job status API
                        try {
                            TaskManager taskManager =
                                    SpringContextHolder.getBean(TaskManager.class);
                            if (taskManager != null) {
                                taskManager.addNote(
                                        job.jobId,
                                        "QUEUED_TIMEOUT: Job waited in queue for "
                                                + (waitTimeMs / 1000)
                                                + " seconds, exceeding the maximum wait time of "
                                                + (maxWaitTimeMs / 1000)
                                                + " seconds.");
                            }
                        } catch (Exception e) {
                            log.error(
                                    "Failed to add timeout note to job {}: {}",
                                    job.jobId,
                                    e.getMessage());
                        }
                    }

                    // Remove from our map
                    jobMap.remove(job.jobId);
                    currentQueueSize = jobQueue.size();

                    // Add to the list of jobs to execute outside the synchronized block
                    jobsToExecute.add(job);
                }
            } catch (Exception e) {
                log.error("Error processing job queue: {}", e.getMessage(), e);
            }
        }

        // Now execute the jobs outside the synchronized block to avoid holding the lock
        for (QueuedJob job : jobsToExecute) {
            executeJob(job);
        }
    }
