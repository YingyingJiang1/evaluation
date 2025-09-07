    public void start() {
        Future<Void> safeStarter = safeStart(this::tokenManagerStart);
        try {
            safeStarter.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("AuthXManager failed to start!", e);
            throw new JedisAuthenticationException("AuthXManager failed to start!",
                    (e instanceof ExecutionException) ? e.getCause() : e);
        }
    }
