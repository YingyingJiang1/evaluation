    private ApiResponse processInterruptJob(ApiRequest apiRequest, Session session) {
        Job job = session.getForegroundJob();
        if (job == null) {
            return new ApiResponse().setState(ApiState.FAILED).setMessage("no foreground job is running");
        }
        job.interrupt();

        Map<String, Object> body = new TreeMap<String, Object>();
        body.put("jobId", job.id());
        body.put("jobStatus", job.status());
        return new ApiResponse()
                .setState(ApiState.SUCCEEDED)
                .setBody(body);
    }
