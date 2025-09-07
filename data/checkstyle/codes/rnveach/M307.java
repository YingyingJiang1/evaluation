    @Override
    public void visitToken(DetailAST ast) {
        switch (ast.getType()) {
            case TokenTypes.CTOR_DEF:
            case TokenTypes.METHOD_DEF:
            case TokenTypes.INSTANCE_INIT:
            case TokenTypes.STATIC_INIT:
            case TokenTypes.COMPACT_CTOR_DEF:
                visitMethodDef();
                break;
            default:
                visitTokenHook(ast);
        }
    }
