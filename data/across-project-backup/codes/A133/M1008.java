    public static StdoutHandler inject(List<CliToken> tokens) {
        List<String> args = StdoutHandler.parseArgs(tokens, NAME);

        GrepCommand grepCommand = new GrepCommand();
        if (cli == null) {
            cli = CLIConfigurator.define(GrepCommand.class);
        }
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, grepCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        int context = grepCommand.getContext();
        int beforeLines = grepCommand.getBeforeLines();
        int afterLines = grepCommand.getAfterLines();
        if (context > 0) {
            if (beforeLines < 1) {
                beforeLines = context;
            }
            if (afterLines < 1) {
                afterLines = context;
            }
        }
        return new GrepHandler(grepCommand.getPattern(), grepCommand.isIgnoreCase(), grepCommand.isInvertMatch(),
                        grepCommand.isRegEx(), grepCommand.isShowLineNumber(), grepCommand.isTrimEnd(), beforeLines,
                        afterLines, grepCommand.getMaxCount());
    }
