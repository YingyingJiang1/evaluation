    private void updateResourceMetrics() {
        try {
            // Get CPU usage
            double cpuUsage = osMXBean.getSystemLoadAverage() / osMXBean.getAvailableProcessors();
            if (cpuUsage < 0) cpuUsage = getAlternativeCpuLoad(); // Fallback if not available

            // Get memory usage
            long heapUsed = memoryMXBean.getHeapMemoryUsage().getUsed();
            long nonHeapUsed = memoryMXBean.getNonHeapMemoryUsage().getUsed();
            long totalUsed = heapUsed + nonHeapUsed;

            long maxMemory = Runtime.getRuntime().maxMemory();
            long totalMemory = Runtime.getRuntime().totalMemory();
            long freeMemory = Runtime.getRuntime().freeMemory();

            double memoryUsage = (double) totalUsed / maxMemory;

            // Create new metrics
            ResourceMetrics metrics =
                    new ResourceMetrics(
                            cpuUsage,
                            memoryUsage,
                            freeMemory,
                            totalMemory,
                            maxMemory,
                            Instant.now());
            latestMetrics.set(metrics);

            // Determine system status
            ResourceStatus newStatus;
            if (cpuUsage > cpuCriticalThreshold || memoryUsage > memoryCriticalThreshold) {
                newStatus = ResourceStatus.CRITICAL;
            } else if (cpuUsage > cpuHighThreshold || memoryUsage > memoryHighThreshold) {
                newStatus = ResourceStatus.WARNING;
            } else {
                newStatus = ResourceStatus.OK;
            }

            // Update status if it changed
            ResourceStatus oldStatus = currentStatus.getAndSet(newStatus);
            if (oldStatus != newStatus) {
                log.info("System resource status changed from {} to {}", oldStatus, newStatus);
                log.info(
                        "Current metrics - CPU: {}%, Memory: {}%, Free Memory: {} MB",
                        String.format("%.1f", cpuUsage * 100),
                        String.format("%.1f", memoryUsage * 100),
                        freeMemory / (1024 * 1024));
            }
        } catch (Exception e) {
            log.error("Error updating resource metrics: {}", e.getMessage(), e);
        }
    }
