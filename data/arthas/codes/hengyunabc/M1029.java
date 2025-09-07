    private void handleJobs() {
        for (Job job : shell.jobController().jobs()) {
            String statusLine = shell.statusLine(job, job.status());
            term.write(statusLine);
        }
        shell.readline();
    }
