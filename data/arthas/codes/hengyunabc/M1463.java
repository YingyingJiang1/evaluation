    public static void arthasStart() {
        if (statUrl == null) {
            return;
        }
        RemoteJob job = new RemoteJob();
        job.appendQueryData("ip", ip);
        job.appendQueryData("version", version);
        if (agentId != null) {
            job.appendQueryData("agentId", agentId);
        }
        job.appendQueryData("command", "start");

        try {
            executorService.execute(job);
        } catch (Throwable t) {
            //
        }
    }
