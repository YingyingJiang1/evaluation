    private void addThread(JvmModel jvmModel) {
        String group = "THREAD";
        jvmModel.addItem(group, "COUNT", threadMXBean.getThreadCount())
                .addItem(group, "DAEMON-COUNT", threadMXBean.getDaemonThreadCount())
                .addItem(group, "PEAK-COUNT", threadMXBean.getPeakThreadCount())
                .addItem(group, "STARTED-COUNT", threadMXBean.getTotalStartedThreadCount())
                .addItem(group, "DEADLOCK-COUNT",getDeadlockedThreadsCount(threadMXBean));
    }
