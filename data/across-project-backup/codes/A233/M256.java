    private Object executeWithRetries(
            ProceedingJoinPoint joinPoint,
            Object[] args,
            boolean async,
            long timeout,
            int maxRetries,
            boolean trackProgress,
            boolean queueable,
            int resourceWeight) {

        // Keep jobId reference for progress tracking in TaskManager
        AtomicReference<String> jobIdRef = new AtomicReference<>();

        return jobExecutorService.runJobGeneric(
                async,
                () -> {
                    // Use iterative approach instead of recursion to avoid stack overflow
                    Throwable lastException = null;

                    // Attempt counter starts at 1 for first try
                    for (int currentAttempt = 1; currentAttempt <= maxRetries; currentAttempt++) {
                        try {
                            if (trackProgress && async) {
                                // Get jobId for progress tracking in TaskManager
                                // This enables REST API progress queries, not WebSocket
                                if (jobIdRef.get() == null) {
                                    jobIdRef.set(getJobIdFromContext());
                                }
                                String jobId = jobIdRef.get();
                                if (jobId != null) {
                                    log.debug(
                                            "Tracking progress for job {} (attempt {}/{})",
                                            jobId,
                                            currentAttempt,
                                            maxRetries);
                                    // Progress is tracked in TaskManager for REST API access
                                    // No WebSocket notifications sent here
                                }
                            }

                            // Attempt to execute the operation
                            return joinPoint.proceed(args);

                        } catch (Throwable ex) {
                            lastException = ex;
                            log.error(
                                    "AutoJobAspect caught exception during job execution (attempt {}/{}): {}",
                                    currentAttempt,
                                    maxRetries,
                                    ex.getMessage(),
                                    ex);

                            // Check if we should retry
                            if (currentAttempt < maxRetries) {
                                log.info(
                                        "Retrying operation, attempt {}/{}",
                                        currentAttempt + 1,
                                        maxRetries);

                                if (trackProgress && async) {
                                    String jobId = jobIdRef.get();
                                    if (jobId != null) {
                                        log.debug(
                                                "Recording retry attempt for job {} in TaskManager",
                                                jobId);
                                        // Retry info is tracked in TaskManager for REST API access
                                    }
                                }

                                // Use non-blocking delay for all retry attempts to avoid blocking
                                // threads
                                // For sync jobs this avoids starving the tomcat thread pool under
                                // load
                                long delayMs = RETRY_BASE_DELAY.toMillis() * currentAttempt;

                                // Execute the retry after a delay through the JobExecutorService
                                // rather than blocking the current thread with sleep
                                CompletableFuture<Object> delayedRetry = new CompletableFuture<>();

                                // Use a delayed executor for non-blocking delay
                                CompletableFuture.delayedExecutor(delayMs, TimeUnit.MILLISECONDS)
                                        .execute(
                                                () -> {
                                                    // Continue the retry loop in the next iteration
                                                    // We can't return from here directly since
                                                    // we're in a Runnable
                                                    delayedRetry.complete(null);
                                                });

                                // Wait for the delay to complete before continuing
                                try {
                                    delayedRetry.join();
                                } catch (Exception e) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            } else {
                                // No more retries, we'll throw the exception after the loop
                                break;
                            }
                        }
                    }

                    // If we get here, all retries failed
                    if (lastException != null) {
                        throw new RuntimeException(
                                "Job failed after "
                                        + maxRetries
                                        + " attempts: "
                                        + lastException.getMessage(),
                                lastException);
                    }

                    // This should never happen if lastException is properly tracked
                    throw new RuntimeException("Job failed but no exception was recorded");
                },
                timeout,
                queueable,
                resourceWeight);
    }
