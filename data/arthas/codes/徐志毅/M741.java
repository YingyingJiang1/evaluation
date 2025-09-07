    public synchronized void restart(CommandProcess process) {
        if (timer == null) {
            Session session = process.session();
            timer = new Timer("Timer-for-arthas-mbean-" + session.getSessionId(), true);
            timer.scheduleAtFixedRate(new MBeanTimerTask(process), 0, getInterval());
        }
    }
