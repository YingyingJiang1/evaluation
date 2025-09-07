    private void completeSingleCommand(Completion completion, LinkedList<CliToken> tokens) {
        ListIterator<CliToken> it = tokens.listIterator();
        while (it.hasNext()) {
            CliToken ct = it.next();
            it.remove();
            if (ct.isText()) {
                final List<CliToken> newTokens = new ArrayList<CliToken>();
                while (it.hasNext()) {
                    newTokens.add(it.next());
                }
                StringBuilder tmp = new StringBuilder();
                for (CliToken token : newTokens) {
                    tmp.append(token.raw());
                }
                final String line = tmp.toString();
                for (CommandResolver resolver : resolvers) {
                    Command command = getCommand(resolver, ct.value());
                    if (command != null) {
                        command.complete(new CommandCompletion(completion, line, newTokens));
                        return;
                    }
                }
                completion.complete(Collections.<String>emptyList());
            }
        }
    }
