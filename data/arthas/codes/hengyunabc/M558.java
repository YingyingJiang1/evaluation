    @Override
    public synchronized void create() {
        if (timer == null) {
            timer = new Timer("Timer-for-arthas-monitor-" + process.session().getSessionId(), true);
            timer.scheduleAtFixedRate(new MonitorTimer(monitorData, process, command.getNumberOfLimit()),
                    0, command.getCycle() * 1000L);
        }
    }
