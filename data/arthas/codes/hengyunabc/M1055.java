    private void injectHandler(List<Function<String, String>> stdoutHandlerChain, List<CliToken> pipelineTokens) {
        if (!pipelineTokens.isEmpty()) {
            StdoutHandler handler = StdoutHandler.inject(pipelineTokens);
            if (handler != null) {
                stdoutHandlerChain.add(handler);
            }
            pipelineTokens.clear();
        }
    }
