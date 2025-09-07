    private static void run(CommandProcess process, String file, boolean live) throws IOException {
        HotSpotDiagnosticMXBean hotSpotDiagnosticMXBean = ManagementFactory
                        .getPlatformMXBean(HotSpotDiagnosticMXBean.class);
        hotSpotDiagnosticMXBean.dumpHeap(file, live);
    }
