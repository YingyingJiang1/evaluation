    @Override
    public void visitToken(DetailAST ast) {
        switch (ast.getType()) {
            case TokenTypes.IDENT:
                if (collect) {
                    processIdent(ast);
                }
                break;
            case TokenTypes.IMPORT:
                processImport(ast);
                break;
            case TokenTypes.STATIC_IMPORT:
                processStaticImport(ast);
                break;
            case TokenTypes.OBJBLOCK:
            case TokenTypes.SLIST:
                currentFrame = currentFrame.push();
                break;
            default:
                collect = true;
                if (processJavadoc) {
                    collectReferencesFromJavadoc(ast);
                }
                break;
        }
    }
