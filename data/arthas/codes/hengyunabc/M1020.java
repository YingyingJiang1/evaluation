    public CommandRegistry registerCommands(List<Command> commands) {
        for (Command command : commands) {
            commandMap.put(command.name(), command);
        }
        return this;
    }
