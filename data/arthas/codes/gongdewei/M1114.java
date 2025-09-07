    @Override
    public Session createSession() {
        Session session = new SessionImpl();
        session.put(Session.COMMAND_MANAGER, commandManager);
        session.put(Session.INSTRUMENTATION, instrumentation);
        session.put(Session.PID, pid);
        //session.put(Session.SERVER, server);
        //session.put(Session.TTY, term);
        String sessionId = UUID.randomUUID().toString();
        session.put(Session.ID, sessionId);

        sessions.put(sessionId, session);
        return session;
    }
