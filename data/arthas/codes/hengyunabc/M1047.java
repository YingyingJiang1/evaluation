    @Override
    public Job createJob(InternalCommandManager commandManager, List<CliToken> tokens, Session session, JobListener jobHandler, Term term, ResultDistributor resultDistributor) {
        checkPermission(session, tokens.get(0));
        int jobId = idGenerator.incrementAndGet();
        StringBuilder line = new StringBuilder();
        for (CliToken arg : tokens) {
            line.append(arg.raw());
        }
        boolean runInBackground = runInBackground(tokens);
        Process process = createProcess(session, tokens, commandManager, jobId, term, resultDistributor);
        process.setJobId(jobId);
        JobImpl job = new JobImpl(jobId, this, process, line.toString(), runInBackground, session, jobHandler);
        jobs.put(jobId, job);
        return job;
    }
