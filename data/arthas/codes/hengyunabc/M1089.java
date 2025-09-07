    private static Command getCommand(CommandResolver commandResolver, String name) {
        List<Command> commands = commandResolver.commands();
        if (commands == null || commands.isEmpty()) {
            return null;
        }

        for (Command command : commands) {
            if (name.equals(command.name())) {
                return command;
            }
        }
        return null;
    }
