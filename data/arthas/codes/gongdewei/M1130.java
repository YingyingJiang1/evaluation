    private ApiResponse processExecRequest(ApiRequest apiRequest, Session session) {
        boolean oneTimeAccess = false;
        if (session.get(ONETIME_SESSION_KEY) != null) {
            oneTimeAccess = true;
        }

        try {
            String commandLine = apiRequest.getCommand();
            Map<String, Object> body = new TreeMap<String, Object>();
            body.put("command", commandLine);

            ApiResponse response = new ApiResponse();
            response.setSessionId(session.getSessionId())
                    .setBody(body);

            if (!session.tryLock()) {
                response.setState(ApiState.REFUSED)
                        .setMessage("Another command is executing.");
                return response;
            }

            int lock = session.getLock();
            PackingResultDistributor packingResultDistributor = null;
            Job job = null;
            try {
                Job foregroundJob = session.getForegroundJob();
                if (foregroundJob != null) {
                    response.setState(ApiState.REFUSED)
                            .setMessage("Another job is running.");
                    logger.info("Another job is running, jobId: {}", foregroundJob.id());
                    return response;
                }

                packingResultDistributor = new PackingResultDistributorImpl(session);
                //distribute result message both to origin session channel and request channel by CompositeResultDistributor
                //ResultDistributor resultDistributor = new CompositeResultDistributorImpl(packingResultDistributor, session.getResultDistributor());
                job = this.createJob(commandLine, session, packingResultDistributor);
                session.setForegroundJob(job);
                updateSessionInputStatus(session, InputStatus.ALLOW_INTERRUPT);

                job.run();

            } catch (Throwable e) {
                logger.error("Exec command failed:" + e.getMessage() + ", command:" + commandLine, e);
                response.setState(ApiState.FAILED).setMessage("Exec command failed:" + e.getMessage());
                return response;
            } finally {
                if (session.getLock() == lock) {
                    session.unLock();
                }
            }

            //wait for job completed or timeout
            Integer timeout = apiRequest.getExecTimeout();
            if (timeout == null || timeout <= 0) {
                timeout = DEFAULT_EXEC_TIMEOUT;
            }
            boolean timeExpired = !waitForJob(job, timeout);
            if (timeExpired) {
                logger.warn("Job is exceeded time limit, force interrupt it, jobId: {}", job.id());
                job.interrupt();
                response.setState(ApiState.INTERRUPTED).setMessage("The job is exceeded time limit, force interrupt");
            } else {
                response.setState(ApiState.SUCCEEDED);
            }

            //packing results
            body.put("jobId", job.id());
            body.put("jobStatus", job.status());
            body.put("timeExpired", timeExpired);
            if (timeExpired) {
                body.put("timeout", timeout);
            }
            body.put("results", packingResultDistributor.getResults());

            response.setSessionId(session.getSessionId())
                    //.setConsumerId(consumerId)
                    .setBody(body);
            return response;
        } finally {
            if (oneTimeAccess) {
                sessionManager.removeSession(session.getSessionId());
            }
        }
    }
