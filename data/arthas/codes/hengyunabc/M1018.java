    private void process(CommandProcess process) {
        AnnotatedCommand instance;
        try {
            instance = clazz.newInstance();
        } catch (Exception e) {
            process.end();
            return;
        }
        CLIConfigurator.inject(process.commandLine(), instance);
        instance.process(process);
        UserStatUtil.arthasUsageSuccess(name(), process.args());
    }
