    private void completeCommands(Completion completion, LinkedList<CliToken> tokens) {
        String prefix = tokens.size() > 0 ? tokens.getFirst().value() : "";
        List<String> names = new LinkedList<String>();
        for (CommandResolver resolver : resolvers) {
            for (Command command : resolver.commands()) {
                String name = command.name();
                boolean hidden = command.cli() != null && command.cli().isHidden();
                if (name.startsWith(prefix) && !names.contains(name) && !hidden) {
                    names.add(name);
                }
            }
        }
        if (names.size() == 1) {
            completion.complete(names.get(0).substring(prefix.length()), true);
        } else {
            String commonPrefix = CompletionUtils.findLongestCommonPrefix(names);
            if (commonPrefix.length() > prefix.length()) {
                completion.complete(commonPrefix.substring(prefix.length()), false);
            } else {
                completion.complete(names);
            }
        }
    }
