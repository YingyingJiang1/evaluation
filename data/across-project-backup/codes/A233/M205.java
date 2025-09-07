    public ResponseEntity<?> runJobGeneric(
            boolean async,
            Supplier<Object> work,
            long customTimeoutMs,
            boolean queueable,
            int resourceWeight) {
        String jobId = UUID.randomUUID().toString();

        // Store the job ID in the request for potential use by other components
        if (request != null) {
            request.setAttribute("jobId", jobId);

            // Also track this job ID in the user's session for authorization purposes
            // This ensures users can only cancel their own jobs
            if (request.getSession() != null) {
                @SuppressWarnings("unchecked")
                java.util.Set<String> userJobIds =
                        (java.util.Set<String>) request.getSession().getAttribute("userJobIds");

                if (userJobIds == null) {
                    userJobIds = new java.util.concurrent.ConcurrentSkipListSet<>();
                    request.getSession().setAttribute("userJobIds", userJobIds);
                }

                userJobIds.add(jobId);
                log.debug("Added job ID {} to user session", jobId);
            }
        }

        // Determine which timeout to use
        long timeoutToUse = customTimeoutMs > 0 ? customTimeoutMs : effectiveTimeoutMs;

        log.debug(
                "Running job with ID: {}, async: {}, timeout: {}ms, queueable: {}, weight: {}",
                jobId,
                async,
                timeoutToUse,
                queueable,
                resourceWeight);

        // Check if we need to queue this job based on resource availability
        boolean shouldQueue =
                queueable
                        && async
                        && // Only async jobs can be queued
                        resourceMonitor.shouldQueueJob(resourceWeight);

        if (shouldQueue) {
            // Queue the job instead of executing immediately
            log.debug(
                    "Queueing job {} due to resource constraints (weight: {})",
                    jobId,
                    resourceWeight);

            taskManager.createTask(jobId);

            // Create a specialized wrapper that updates the TaskManager
            Supplier<Object> wrappedWork =
                    () -> {
                        try {
                            Object result = work.get();
                            processJobResult(jobId, result);
                            return result;
                        } catch (Exception e) {
                            log.error(
                                    "Error executing queued job {}: {}", jobId, e.getMessage(), e);
                            taskManager.setError(jobId, e.getMessage());
                            throw e;
                        }
                    };

            // Queue the job and get the future
            CompletableFuture<ResponseEntity<?>> future =
                    jobQueue.queueJob(jobId, resourceWeight, wrappedWork, timeoutToUse);

            // Return immediately with job ID
            return ResponseEntity.ok().body(new JobResponse<>(true, jobId, null));
        } else if (async) {
            taskManager.createTask(jobId);
            executor.execute(
                    () -> {
                        try {
                            log.debug(
                                    "Running async job {} with timeout {} ms", jobId, timeoutToUse);

                            // Execute with timeout
                            Object result = executeWithTimeout(() -> work.get(), timeoutToUse);
                            processJobResult(jobId, result);
                        } catch (TimeoutException te) {
                            log.error("Job {} timed out after {} ms", jobId, timeoutToUse);
                            taskManager.setError(jobId, "Job timed out");
                        } catch (Exception e) {
                            log.error("Error executing job {}: {}", jobId, e.getMessage(), e);
                            taskManager.setError(jobId, e.getMessage());
                        }
                    });

            return ResponseEntity.ok().body(new JobResponse<>(true, jobId, null));
        } else {
            try {
                log.debug("Running sync job with timeout {} ms", timeoutToUse);

                // Execute with timeout
                Object result = executeWithTimeout(() -> work.get(), timeoutToUse);

                // If the result is already a ResponseEntity, return it directly
                if (result instanceof ResponseEntity) {
                    return (ResponseEntity<?>) result;
                }

                // Process different result types
                return handleResultForSyncJob(result);
            } catch (TimeoutException te) {
                log.error("Synchronous job timed out after {} ms", timeoutToUse);
                return ResponseEntity.internalServerError()
                        .body(Map.of("error", "Job timed out after " + timeoutToUse + " ms"));
            } catch (Exception e) {
                log.error("Error executing synchronous job: {}", e.getMessage(), e);
                // Construct a JSON error response
                return ResponseEntity.internalServerError()
                        .body(Map.of("error", "Job failed: " + e.getMessage()));
            }
        }
    }
