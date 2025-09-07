    @Override
    public Job toBackground() {
        if (!this.runInBackground.get()) {
            // run in foreground mode
            if (runInBackground.compareAndSet(false, true)) {
                process.toBackground();
                if (statusUpdateHandler != null) {
                    statusUpdateHandler.handle(process.status());
                }
                jobHandler.onBackground(this);
            }
        }

//        shell.setForegroundJob(null);
//        jobHandler.onBackground(this);
        return this;
    }
