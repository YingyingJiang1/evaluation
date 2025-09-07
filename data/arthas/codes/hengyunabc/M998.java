    public static StdoutHandler inject(List<CliToken> tokens) {
        List<String> args = StdoutHandler.parseArgs(tokens, NAME);
        CommandLine commandLine = CLIs.create(NAME)
                .addOption(new Option().setShortName("l").setFlag(true))
                .parse(args);
        boolean lineMode = commandLine.isFlagEnabled("l");
        return new WordCountHandler(lineMode);
    }
