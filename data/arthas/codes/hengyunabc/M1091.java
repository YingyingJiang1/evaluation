    @Override
    public synchronized ShellServer registerCommandResolver(CommandResolver resolver) {
        resolvers.add(0, resolver);
        return this;
    }
