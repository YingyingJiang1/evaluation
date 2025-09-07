    public void close(String reason) {
        if (term != null) {
            try {
                term.write("session (" + session.getSessionId() + ") is closed because " + reason + "\n");
            } catch (Throwable t) {
                // sometimes an NPE will be thrown during shutdown via web-socket,
                // this ensures the shutdown process is finished properly
                // https://github.com/alibaba/arthas/issues/320
                logger.error("Error writing data:", t);
            }
            term.close();
        } else {
            jobController.close(closedFutureHandler());
        }
    }
