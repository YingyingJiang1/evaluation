    @Override
    public List<Command> commands() {
        return Arrays.asList(CommandBuilder.command("exit").processHandler(handler).build(),
                             CommandBuilder.command("quit").processHandler(handler).build(),
                             CommandBuilder.command("jobs").processHandler(handler).build(),
                             CommandBuilder.command("fg").processHandler(handler).build(),
                             CommandBuilder.command("bg").processHandler(handler).build(),
                             CommandBuilder.command("kill").processHandler(handler).build(),
                             CommandBuilder.command(PlainTextHandler.NAME).processHandler(handler).build(),
                             CommandBuilder.command(GrepHandler.NAME).processHandler(handler).build(),
                             CommandBuilder.command(WordCountHandler.NAME).processHandler(handler).build());
    }
