    @Override
    public void process(final CommandProcess process) {

        Session session = process.session();
        timer = new Timer("Timer-for-arthas-dashboard-" + session.getSessionId(), true);

        // ctrl-C support
        process.interruptHandler(new DashboardInterruptHandler(process, timer));

        /*
         * 通过handle回调，在suspend和end时停止timer，resume时重启timer
         */
        Handler<Void> stopHandler = new Handler<Void>() {
            @Override
            public void handle(Void event) {
                stop();
            }
        };

        Handler<Void> restartHandler = new Handler<Void>() {
            @Override
            public void handle(Void event) {
                restart(process);
            }
        };
        process.suspendHandler(stopHandler);
        process.resumeHandler(restartHandler);
        process.endHandler(stopHandler);

        // q exit support
        process.stdinHandler(new QExitHandler(process));

        // start the timer
        timer.scheduleAtFixedRate(new DashboardTimerTask(process), 0, getInterval());
    }
