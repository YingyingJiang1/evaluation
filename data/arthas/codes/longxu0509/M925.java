    @Override
    public void complete(Completion completion) {
        List<CliToken> tokens = completion.lineTokens();
        String token = tokens.get(tokens.size() - 1).value();

        if (token.startsWith("-")) {
            super.complete(completion);
            return;
        }
        List<String> cmd = Arrays.asList("start", "status", "dump", "stop");
        CompletionUtils.complete(completion, cmd);
    }
