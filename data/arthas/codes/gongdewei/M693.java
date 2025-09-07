    private void addOperatingSystem(JvmModel jvmModel) {
        String group = "OPERATING-SYSTEM";
        jvmModel.addItem(group,"OS", operatingSystemMXBean.getName())
                .addItem(group,"ARCH", operatingSystemMXBean.getArch())
                .addItem(group,"PROCESSORS-COUNT", operatingSystemMXBean.getAvailableProcessors())
                .addItem(group,"LOAD-AVERAGE", operatingSystemMXBean.getSystemLoadAverage())
                .addItem(group,"VERSION", operatingSystemMXBean.getVersion());
    }
