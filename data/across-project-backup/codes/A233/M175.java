    private void updateQueueCapacity() {
        try {
            // Calculate new capacity once and cache the result
            int newCapacity =
                    resourceMonitor.calculateDynamicQueueCapacity(
                            baseQueueCapacity, minQueueCapacity);

            int currentCapacity = getQueueCapacity();
            if (newCapacity != currentCapacity) {
                log.debug(
                        "Updating job queue capacity from {} to {}", currentCapacity, newCapacity);

                synchronized (queueLock) {
                    // Double-check that capacity still needs to be updated
                    // Use the cached currentCapacity to avoid calling getQueueCapacity() again
                    if (newCapacity != currentCapacity) {
                        // Create new queue with updated capacity
                        BlockingQueue<QueuedJob> newQueue = new LinkedBlockingQueue<>(newCapacity);

                        // Transfer jobs from old queue to new queue
                        jobQueue.drainTo(newQueue);
                        jobQueue = newQueue;

                        currentQueueSize = jobQueue.size();
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error updating queue capacity: {}", e.getMessage(), e);
        }
    }
