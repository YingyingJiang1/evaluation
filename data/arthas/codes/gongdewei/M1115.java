    @Override
    public void close() {
        //TODO clear resources while shutdown arthas
        closed = true;
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdownNow();
        }

        ArrayList<Session> sessions = new ArrayList<Session>(this.sessions.values());
        for (Session session : sessions) {
            SharingResultDistributor resultDistributor = session.getResultDistributor();
            if (resultDistributor != null) {
                resultDistributor.appendResult(new MessageModel("arthas server is going to shutdown."));
            }
            logger.info("Removing session before shutdown: {}, last access time: {}", session.getSessionId(), session.getLastAccessTime());
            this.removeSession(session.getSessionId());
        }

        jobController.close();
    }
