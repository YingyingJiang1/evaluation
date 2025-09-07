    @Override
    public void leaveToken(DetailAST ast) {
        switch (ast.getType()) {
            case TokenTypes.SLIST:
                leaveSlist(ast);
                break;
            case TokenTypes.LITERAL_NEW:
                leaveLiteralNew(ast);
                break;
            case TokenTypes.OBJBLOCK:
                final int parentType = ast.getParent().getType();
                if (!astTypeIsClassOrEnumOrRecordDef(parentType)) {
                    currentFrame = currentFrame.getParent();
                }
                break;
            case TokenTypes.VARIABLE_DEF:
            case TokenTypes.PARAMETER_DEF:
            case TokenTypes.RECORD_COMPONENT_DEF:
            case TokenTypes.METHOD_CALL:
            case TokenTypes.PATTERN_VARIABLE_DEF:
                break;
            default:
                currentFrame = currentFrame.getParent();
                break;
        }
    }
