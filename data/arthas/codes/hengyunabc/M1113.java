    @Override
    public List<CommandResolver> getCommandResolvers() {
        InternalCommandManager commandManager = (InternalCommandManager) data.get(COMMAND_MANAGER);
        return commandManager.getResolvers();
    }
