    @Override
    public synchronized Process setSession(Session session) {
        this.session = session;
        return this;
    }
