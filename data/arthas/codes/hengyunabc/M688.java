    @Override
    protected AdviceListener getAdviceListener(CommandProcess process) {
        final AdviceListener listener = new MonitorAdviceListener(this, process, GlobalOptions.verbose || this.verbose);
        /*
         * 通过handle回调，在suspend时停止timer，resume时重启timer
         */
        process.suspendHandler(new Handler<Void>() {
            @Override
            public void handle(Void event) {
                listener.destroy();
            }
        });
        process.resumeHandler(new Handler<Void>() {
            @Override
            public void handle(Void event) {
                listener.create();
            }
        });
        return listener;
    }
