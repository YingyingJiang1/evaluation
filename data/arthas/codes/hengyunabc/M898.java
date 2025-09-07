    private Command findCommand(List<Command> commands) {
        for (Command command : commands) {
            if (command.name().equals(cmd)) {
                return command;
            }
        }
        return null;
    }
