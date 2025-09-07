    private void handleForeground(List<CliToken> tokens) {
        String arg = TokenUtils.findSecondTokenText(tokens);
        Job job;
        if (arg == null) {
            job = shell.getForegroundJob();
        } else {
            job = shell.jobController().getJob(getJobId(arg));
        }
        if (job == null) {
            term.write(arg + " : no such job\n");
            shell.readline();
        } else {
            if (job.getSession() != shell.session()) {
                term.write("job " + job.id() + " doesn't belong to this session, so can not fg it\n");
                shell.readline();
            } else if (job.status() == ExecStatus.STOPPED) {
                job.resume(true);
            } else if (job.status() == ExecStatus.RUNNING) {
                // job is running
                job.toForeground();
            } else {
                term.write("job " + job.id() + " is already terminated, so can not fg it\n");
                shell.readline();
            }
        }
    }
