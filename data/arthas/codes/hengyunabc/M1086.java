    public void complete(final Completion completion) {
        List<CliToken> lineTokens = completion.lineTokens();
        int index = findLastPipe(lineTokens);
        LinkedList<CliToken> tokens = new LinkedList<CliToken>(lineTokens.subList(index + 1, lineTokens.size()));

        // Remove any leading white space
        while (tokens.size() > 0 && tokens.getFirst().isBlank()) {
            tokens.removeFirst();
        }

        // > 1 means it's a text token followed by something else
        if (tokens.size() > 1) {
            completeSingleCommand(completion, tokens);
        } else {
            completeCommands(completion, tokens);
        }
    }
