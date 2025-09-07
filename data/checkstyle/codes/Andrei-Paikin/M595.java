    @Override
    public void visitToken(DetailAST ast) {
        switch (ast.getType()) {
            case TokenTypes.CLASS_DEF:
            case TokenTypes.INTERFACE_DEF:
            case TokenTypes.ENUM_DEF:
            case TokenTypes.ANNOTATION_DEF:
            case TokenTypes.RECORD_DEF:
                checkTypeDefinition(ast);
                break;
            case TokenTypes.VARIABLE_DEF:
                checkVariableDefinition(ast);
                break;
            case TokenTypes.ENUM_CONSTANT_DEF:
                checkEnumConstant(ast);
                break;
            default:
                checkTypeMember(ast);
                break;
        }
    }
