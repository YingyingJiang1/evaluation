    private boolean isInLatestScopeDefinition(DetailAST methodDef) {
        boolean result = false;

        if (!counters.isEmpty()) {
            final DetailAST latestDefinition = counters.peek().getScopeDefinition();

            result = latestDefinition == methodDef.getParent().getParent();
        }

        return result;
    }
