    private HttpSession newHttpSession() {
        SimpleHttpSession session = new SimpleHttpSession();
        this.sessions.put(session.getId(), session);
        return session;
    }
