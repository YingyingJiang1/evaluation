    @Override
    public void process(CommandProcess process) {
        if (message != null) {
            process.appendResult(new EchoModel(message));
        }

        process.end();
    }
