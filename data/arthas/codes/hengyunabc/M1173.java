    @Override
    public void accept(final Completion completion) {
        try {
            final String line = io.termd.core.util.Helper.fromCodePoints(completion.line());
            final List<CliToken> tokens = Collections.unmodifiableList(CliTokens.tokenize(line));
            com.taobao.arthas.core.shell.cli.Completion comp = new CompletionAdaptor(line, tokens, completion, session);
            completionHandler.handle(comp);
        } catch (Throwable t) {
            // t.printStackTrace();
            logger.error("completion error", t);
        }
    }
