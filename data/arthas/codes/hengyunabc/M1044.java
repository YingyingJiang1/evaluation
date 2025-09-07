    @Override
    public Job createJob(InternalCommandManager commandManager, List<CliToken> tokens, Session session, JobListener jobHandler, Term term, ResultDistributor resultDistributor) {
        final Job job = super.createJob(commandManager, tokens, session, jobHandler, term, resultDistributor);

        /*
         * 达到超时时间将会停止job
         */
        JobTimeoutTask jobTimeoutTask = new JobTimeoutTask(job);
        long jobTimeoutInSecond = getJobTimeoutInSecond();
        Date timeoutDate = new Date(System.currentTimeMillis() + (jobTimeoutInSecond * 1000));
        ArthasBootstrap.getInstance().getScheduledExecutorService().schedule(jobTimeoutTask, jobTimeoutInSecond, TimeUnit.SECONDS);
        jobTimeoutTaskMap.put(job.id(), jobTimeoutTask);
        job.setTimeoutDate(timeoutDate);

        return job;
    }
