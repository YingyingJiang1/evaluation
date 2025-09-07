    private void interruptJob(String message) {
        Job job = session.getForegroundJob();
        if (job != null) {
            logger.warn(message+", current job was interrupted.", job.id());
            job.interrupt();
            pendingResultQueue.offer(new MessageModel(message+", current job was interrupted."));
        }
    }
