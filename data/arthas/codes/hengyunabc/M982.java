    public static boolean completeMethodName(Completion completion) {
        List<CliToken> tokens = completion.lineTokens();
        String lastToken = completion.lineTokens().get(tokens.size() - 1).value();

        if (StringUtils.isBlank(lastToken)) {
            lastToken = "";
        }

        // retrieve the class name
        String className;
        if (StringUtils.isBlank(lastToken)) {
            // tokens = { " ", "CLASS_NAME", " "}
            className = tokens.get(tokens.size() - 2).value();
        } else {
            // tokens = { " ", "CLASS_NAME", " ", "PARTIAL_METHOD_NAME"}
            className = tokens.get(tokens.size() - 3).value();
        }

        Set<Class<?>> results = SearchUtils.searchClassOnly(completion.session().getInstrumentation(), className, 2);
        if (results.size() != 1) {
            // no class found or multiple class found
            completion.complete(Collections.<String>emptyList());
            return true;
        }

        Class<?> clazz = results.iterator().next();

        List<String> res = new ArrayList<String>();

        for (Method method : clazz.getDeclaredMethods()) {
            if (StringUtils.isBlank(lastToken)) {
                res.add(method.getName());
            } else if (method.getName().startsWith(lastToken)) {
                res.add(method.getName());
            }
        }
        res.add("<init>");

        if (res.size() == 1) {
            completion.complete(res.get(0).substring(lastToken.length()), true);
            return true;
        } else {
            CompletionUtils.complete(completion, res);
            return true;
        }
    }
