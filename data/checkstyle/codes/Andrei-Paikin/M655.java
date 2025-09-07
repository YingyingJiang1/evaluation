    private void checkLambda(DetailAST ast, DetailAST currentStatement) {
        int countOfSemiInCurrentLambda = countOfSemiInLambda.pop();
        countOfSemiInCurrentLambda++;
        countOfSemiInLambda.push(countOfSemiInCurrentLambda);
        if (!inForHeader && countOfSemiInCurrentLambda > 1
                && isOnTheSameLine(currentStatement,
                lastStatementEnd, forStatementEnd,
                lambdaStatementEnd)) {
            log(ast, MSG_KEY);
        }
    }
