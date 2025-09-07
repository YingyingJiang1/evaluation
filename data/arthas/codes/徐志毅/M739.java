    private void listAttribute(final CommandProcess process) {
        Session session = process.session();
        timer = new Timer("Timer-for-arthas-mbean-" + session.getSessionId(), true);

        // ctrl-C support
        process.interruptHandler(new MBeanInterruptHandler(process, timer));

        // 通过handle回调，在suspend和end时停止timer，resume时重启timer
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
        if (getInterval() > 0) {
            timer.scheduleAtFixedRate(new MBeanTimerTask(process), 0, getInterval());
        } else {
            timer.schedule(new MBeanTimerTask(process), 0);
        }

        //异步执行，这里不能调用process.end()，在timer task中结束命令执行
    }
