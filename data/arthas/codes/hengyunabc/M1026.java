    private void handleKill(List<CliToken> tokens) {
        String arg = TokenUtils.findSecondTokenText(tokens);
        if (arg == null) {
            term.write("kill: usage: kill job_id\n");
            shell.readline();
            return;
        }
        Job job = shell.jobController().getJob(getJobId(arg));
        if (job == null) {
            term.write(arg + " : no such job\n");
            shell.readline();
        } else {
            job.terminate();
            term.write("kill job " + job.id() + " success\n");
            shell.readline();
        }
    }
