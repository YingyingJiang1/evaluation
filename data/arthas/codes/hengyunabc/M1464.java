    private static void arthasUsage(String cmd, String detail) {
        RemoteJob job = new RemoteJob();
        job.appendQueryData("ip", ip);
        job.appendQueryData("version", version);
        if (agentId != null) {
            job.appendQueryData("agentId", agentId);
        }
        job.appendQueryData("command", URLEncoder.encode(cmd));
        if (detail != null) {
            job.appendQueryData("arguments", URLEncoder.encode(detail));
        }

        try {
            executorService.execute(job);
        } catch (Throwable t) {
            //
        }
    }
