    private boolean isNotRelevantSituation(DetailAST ast, int currentType) {
        final int parentType = ast.getParent().getType();
        final boolean result;
        switch (parentType) {
            case TokenTypes.DOT:
                result = currentType == TokenTypes.STAR;
                break;
            case TokenTypes.LITERAL_DEFAULT:
            case TokenTypes.LITERAL_CASE:
            case TokenTypes.CASE_GROUP:
                result = true;
                break;
            case TokenTypes.FOR_EACH_CLAUSE:
                result = ignoreEnhancedForColon;
                break;
            case TokenTypes.EXPR:
                result = currentType == TokenTypes.LITERAL_SWITCH;
                break;
            case TokenTypes.ARRAY_INIT:
            case TokenTypes.ANNOTATION_ARRAY_INIT:
                result = currentType == TokenTypes.RCURLY;
                break;
            default:
                result = isEmptyBlock(ast, parentType)
                    || allowEmptyTypes && isEmptyType(ast);
        }
        return result;
    }
