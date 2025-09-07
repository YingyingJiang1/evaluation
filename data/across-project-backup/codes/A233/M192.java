    private double getAlternativeCpuLoad() {
        try {
            // Try to get CPU time if available through reflection
            // This is a fallback since we can't directly cast to platform-specific classes
            try {
                java.lang.reflect.Method m =
                        osMXBean.getClass().getDeclaredMethod("getProcessCpuLoad");
                m.setAccessible(true);
                return (double) m.invoke(osMXBean);
            } catch (Exception e) {
                // Try the older method
                try {
                    java.lang.reflect.Method m =
                            osMXBean.getClass().getDeclaredMethod("getSystemCpuLoad");
                    m.setAccessible(true);
                    return (double) m.invoke(osMXBean);
                } catch (Exception e2) {
                    log.trace(
                            "Could not get CPU load through reflection, assuming moderate load (0.5)");
                    return 0.5;
                }
            }
        } catch (Exception e) {
            log.trace("Could not get CPU load, assuming moderate load (0.5)");
            return 0.5; // Default to moderate load
        }
    }
