    @Override
    public void visitJavadocToken(DetailNode ast) {
        final DetailAST blockCommentAst = getBlockCommentAst();
        if (BlockCommentPosition.isOnMethod(blockCommentAst)) {
            final DetailAST methodDef = getParentAst(blockCommentAst, TokenTypes.METHOD_DEF);
            if (methodDef != null
                    && isSetterMethod(methodDef)
                    && isMethodOfScrapedModule(methodDef)) {
                final String methodName = methodDef.findFirstToken(TokenTypes.IDENT).getText();
                final String propertyName = getPropertyName(methodName);
                JAVADOC_FOR_MODULE_OR_PROPERTY.put(propertyName, ast);
            }

        }
        else if (BlockCommentPosition.isOnClass(blockCommentAst)) {
            final DetailAST classDef = getParentAst(blockCommentAst, TokenTypes.CLASS_DEF);
            if (classDef != null) {
                final String className = classDef.findFirstToken(TokenTypes.IDENT).getText();
                if (className.equals(moduleName)) {
                    JAVADOC_FOR_MODULE_OR_PROPERTY.put(moduleName, ast);
                }
            }
        }
    }
