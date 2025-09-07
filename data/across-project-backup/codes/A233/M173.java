    public CompletableFuture<ResponseEntity<?>> queueJob(
            String jobId, int resourceWeight, Supplier<Object> work, long timeoutMs) {

        // Create a CompletableFuture to track this job's completion
        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();

        // Create the queued job
        QueuedJob job =
                new QueuedJob(jobId, resourceWeight, work, timeoutMs, Instant.now(), future, false);

        // Store in our map for lookup
        jobMap.put(jobId, job);

        // Update stats
        totalQueuedJobs++;

        // Synchronize access to the queue
        synchronized (queueLock) {
            currentQueueSize = jobQueue.size();

            // Try to add to the queue
            try {
                boolean added = jobQueue.offer(job, 5, TimeUnit.SECONDS);
                if (!added) {
                    log.warn("Queue full, rejecting job {}", jobId);
                    rejectedJobs++;
                    future.completeExceptionally(
                            new RuntimeException("Job queue full, please try again later"));
                    jobMap.remove(jobId);
                    return future;
                }

                log.debug(
                        "Job {} queued for execution (weight: {}, queue size: {})",
                        jobId,
                        resourceWeight,
                        jobQueue.size());

                return future;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                future.completeExceptionally(new RuntimeException("Job queue interrupted"));
                jobMap.remove(jobId);
                return future;
            }
        }
    }
