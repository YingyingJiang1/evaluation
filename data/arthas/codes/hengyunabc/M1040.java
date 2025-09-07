    @Override
    public void handle(Future<TermServer> ar) {
        if (ar.failed()) {
            failed.set(true);
        }

        if (count.decrementAndGet() == 0) {
            if (failed.get()) {
                listenHandler.handle(Future.<Void>failedFuture(ar.cause()));
                for (TermServer termServer : toStart) {
                    termServer.close();
                }
            } else {
                shellServer.setClosed(false);
                shellServer.setTimer();
                listenHandler.handle(Future.<Void>succeededFuture());
            }
        }
    }
