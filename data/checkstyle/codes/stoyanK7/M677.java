    private static List<DetailAST> getVariableUsageExpressionsInsideSwitchBlock(DetailAST block,
                                                                            DetailAST variable) {
        final Optional<DetailAST> firstToken = TokenUtil.findFirstTokenByPredicate(block, child -> {
            return child.getType() == TokenTypes.SWITCH_RULE
                    || child.getType() == TokenTypes.CASE_GROUP;
        });

        final List<DetailAST> variableUsageExpressions = new ArrayList<>();

        firstToken.ifPresent(token -> {
            TokenUtil.forEachChild(block, token.getType(), child -> {
                final DetailAST lastNodeInCaseGroup = child.getLastChild();
                if (isChild(lastNodeInCaseGroup, variable)) {
                    variableUsageExpressions.add(lastNodeInCaseGroup);
                }
            });
        });

        return variableUsageExpressions;
    }
