    public synchronized void restart(CommandProcess process) {
        if (timer == null) {
            Session session = process.session();
            timer = new Timer("Timer-for-arthas-dashboard-" + session.getSessionId(), true);
            timer.scheduleAtFixedRate(new DashboardTimerTask(process), 0, getInterval());
        }
    }
