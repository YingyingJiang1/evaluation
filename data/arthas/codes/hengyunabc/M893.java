    @Override
    public void process(CommandProcess process) {
        List<Command> commands = allCommands(process.session());
        Command targetCmd = findCommand(commands);
        if (targetCmd == null) {
            process.appendResult(createHelpModel(commands));
        } else {
            process.appendResult(createHelpDetailModel(targetCmd));
        }
        process.end();
    }
