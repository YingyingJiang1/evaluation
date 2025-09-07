    public Map<String, Object> captureServerMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        try {
            // Application version
            metrics.put("app_version", appVersion);
            String deploymentType = "JAR"; // default
            if ("true".equalsIgnoreCase(env.getProperty("BROWSER_OPEN"))) {
                deploymentType = "EXE";
            } else if (isRunningInDocker()) {
                deploymentType = "DOCKER";
            }
            metrics.put("deployment_type", deploymentType);
            metrics.put("mounted_config_dir", configDirMounted);

            // System info
            metrics.put("os_name", System.getProperty("os.name"));
            metrics.put("os_version", System.getProperty("os.version"));
            metrics.put("java_version", System.getProperty("java.version"));
            metrics.put("user_name", System.getProperty("user.name"));
            metrics.put("user_home", System.getProperty("user.home"));
            metrics.put("user_dir", System.getProperty("user.dir"));

            // CPU and Memory
            metrics.put("cpu_cores", Runtime.getRuntime().availableProcessors());
            metrics.put("total_memory", Runtime.getRuntime().totalMemory());
            metrics.put("free_memory", Runtime.getRuntime().freeMemory());

            // Network and Server Identity
            InetAddress localHost = InetAddress.getLocalHost();
            metrics.put("ip_address", localHost.getHostAddress());
            metrics.put("hostname", localHost.getHostName());
            metrics.put("mac_address", getMacAddress());

            // JVM info
            metrics.put("jvm_vendor", System.getProperty("java.vendor"));
            metrics.put("jvm_version", System.getProperty("java.vm.version"));

            // Locale and Timezone
            metrics.put("system_language", System.getProperty("user.language"));
            metrics.put("system_country", System.getProperty("user.country"));
            metrics.put("timezone", TimeZone.getDefault().getID());
            metrics.put("locale", Locale.getDefault().toString());

            // Disk info
            File root = new File(".");
            metrics.put("total_disk_space", root.getTotalSpace());
            metrics.put("free_disk_space", root.getFreeSpace());

            // Process info
            metrics.put("process_id", ProcessHandle.current().pid());

            // JVM metrics
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            metrics.put("jvm_uptime_ms", runtimeMXBean.getUptime());
            metrics.put("jvm_start_time", runtimeMXBean.getStartTime());

            // Memory metrics
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            metrics.put("heap_memory_usage", memoryMXBean.getHeapMemoryUsage().getUsed());
            metrics.put("non_heap_memory_usage", memoryMXBean.getNonHeapMemoryUsage().getUsed());

            // CPU metrics
            OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
            metrics.put("system_load_average", osMXBean.getSystemLoadAverage());

            // Thread metrics
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            metrics.put("thread_count", threadMXBean.getThreadCount());
            metrics.put("daemon_thread_count", threadMXBean.getDaemonThreadCount());
            metrics.put("peak_thread_count", threadMXBean.getPeakThreadCount());

            // Garbage collection metrics
            for (GarbageCollectorMXBean gcBean : ManagementFactory.getGarbageCollectorMXBeans()) {
                metrics.put("gc_" + gcBean.getName() + "_count", gcBean.getCollectionCount());
                metrics.put("gc_" + gcBean.getName() + "_time", gcBean.getCollectionTime());
            }

            // Network interfaces
            metrics.put("network_interfaces", getNetworkInterfacesInfo());

            // Docker detection and stats
            boolean isDocker = isRunningInDocker();
            if (isDocker) {
                metrics.put("docker_metrics", getDockerMetrics());
            }
            metrics.put("application_properties", captureApplicationProperties());

            if (userService != null) {
                metrics.put("total_users_created", userService.getTotalUsersCount());
            }

        } catch (Exception e) {
            metrics.put("error", e.getMessage());
        }

        return metrics;
    }
