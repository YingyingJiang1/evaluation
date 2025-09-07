    @DeleteMapping("/api/v1/general/job/{jobId}")
    public ResponseEntity<?> cancelJob(@PathVariable("jobId") String jobId) {
        log.debug("Request to cancel job: {}", jobId);

        // Verify that this job belongs to the current user
        // We can use the current request's session to validate ownership
        Object sessionJobIds = request.getSession().getAttribute("userJobIds");
        if (sessionJobIds == null
                || !(sessionJobIds instanceof java.util.Set)
                || !((java.util.Set<?>) sessionJobIds).contains(jobId)) {
            // Either no jobs in session or jobId doesn't match user's jobs
            log.warn("Unauthorized attempt to cancel job: {}", jobId);
            return ResponseEntity.status(403)
                    .body(Map.of("message", "You are not authorized to cancel this job"));
        }

        // First check if the job is in the queue
        boolean cancelled = false;
        int queuePosition = -1;

        if (jobQueue.isJobQueued(jobId)) {
            queuePosition = jobQueue.getJobPosition(jobId);
            cancelled = jobQueue.cancelJob(jobId);
            log.info("Cancelled queued job: {} (was at position {})", jobId, queuePosition);
        }

        // If not in queue or couldn't cancel, try to cancel in TaskManager
        if (!cancelled) {
            JobResult result = taskManager.getJobResult(jobId);
            if (result != null && !result.isComplete()) {
                // Mark as error with cancellation message
                taskManager.setError(jobId, "Job was cancelled by user");
                cancelled = true;
                log.info("Marked job as cancelled in TaskManager: {}", jobId);
            }
        }

        if (cancelled) {
            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Job cancelled successfully",
                            "wasQueued",
                            queuePosition >= 0,
                            "queuePosition",
                            queuePosition >= 0 ? queuePosition : "n/a"));
        } else {
            // Job not found or already complete
            JobResult result = taskManager.getJobResult(jobId);
            if (result == null) {
                return ResponseEntity.notFound().build();
            } else if (result.isComplete()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Cannot cancel job that is already complete"));
            } else {
                return ResponseEntity.internalServerError()
                        .body(Map.of("message", "Failed to cancel job for unknown reason"));
            }
        }
    }
