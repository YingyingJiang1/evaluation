    public int calculateDynamicQueueCapacity(int baseCapacity, int minCapacity) {
        ResourceMetrics metrics = latestMetrics.get();
        ResourceStatus status = currentStatus.get();

        // Simple linear reduction based on memory and CPU load
        double capacityFactor =
                switch (status) {
                    case OK -> 1.0;
                    case WARNING -> 0.6;
                    case CRITICAL -> 0.3;
                };

        // Apply additional reduction based on specific memory pressure
        if (metrics.memoryUsage > 0.8) {
            capacityFactor *= 0.5; // Further reduce capacity under memory pressure
        }

        // Calculate capacity with minimum safeguard
        int capacity = (int) Math.max(minCapacity, Math.ceil(baseCapacity * capacityFactor));

        log.debug(
                "Dynamic queue capacity: {} (base: {}, factor: {:.2f}, status: {})",
                capacity,
                baseCapacity,
                capacityFactor,
                status);

        return capacity;
    }
