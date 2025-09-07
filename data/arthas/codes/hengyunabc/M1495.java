    public static String getStartDateTime() {
        try {
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            long startTime = runtimeMXBean.getStartTime();
            Instant startInstant = Instant.ofEpochMilli(startTime);
            LocalDateTime startDateTime = LocalDateTime.ofInstant(startInstant, ZoneId.systemDefault());
            return DATE_TIME_FORMATTER.format(startDateTime);
        } catch (Throwable e) {
            return "unknown";
        }
    }
