    private boolean isIgnoredParam(DetailAST ast, String name) {
        return isIgnoredSetterParam(ast, name)
            || isIgnoredConstructorParam(ast)
            || isIgnoredParamOfAbstractMethod(ast);
    }
