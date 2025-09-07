    @Override
    public boolean deliver(int key) {
        Term term = shell.term();

        Job job = shell.getForegroundJob();
        if (job != null) {
            term.echo(shell.statusLine(job, ExecStatus.STOPPED));
            job.suspend();
        }

        return true;
    }
