    @Override
    public synchronized ShellServer registerTermServer(TermServer termServer) {
        termServers.add(termServer);
        return this;
    }
