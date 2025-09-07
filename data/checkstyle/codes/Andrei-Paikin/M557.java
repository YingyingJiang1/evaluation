    private void checkToken(DetailAST ast, DetailAST nextToken) {
        final int astType = ast.getType();
        switch (astType) {
            case TokenTypes.VARIABLE_DEF:
                processVariableDef(ast, nextToken);
                break;
            case TokenTypes.IMPORT:
            case TokenTypes.STATIC_IMPORT:
                processImport(ast, nextToken);
                break;
            case TokenTypes.PACKAGE_DEF:
                processPackage(ast, nextToken);
                break;
            default:
                if (nextToken.getType() == TokenTypes.RCURLY) {
                    if (hasNotAllowedTwoEmptyLinesBefore(nextToken)) {
                        final DetailAST result = getLastElementBeforeEmptyLines(ast,
                                nextToken.getLineNo());
                        log(result, MSG_MULTIPLE_LINES_AFTER, result.getText());
                    }
                }
                else if (!hasEmptyLineAfter(ast)) {
                    log(nextToken, MSG_SHOULD_BE_SEPARATED,
                        nextToken.getText());
                }
        }
    }
