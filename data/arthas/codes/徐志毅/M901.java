    @Override
    public void complete(Completion completion) {
        int argumentIndex = CompletionUtils.detectArgumentIndex(completion);
        List<CliToken> lineTokens = completion.lineTokens();
        if (argumentIndex == 1) {
            String laseToken = TokenUtils.getLast(lineTokens).value().trim();
            //prefix match options-name
            String pattern = "^" + laseToken + ".*";
            Collection<String> optionNames = findOptionNames(new RegexMatcher(pattern));
            CompletionUtils.complete(completion, optionNames);
        } else {
            super.complete(completion);
        }
    }
