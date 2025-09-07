    @Override
    public void visitToken(DetailAST ast) {
        if (CheckUtil.isPackageInfo(getFilePath()) && !hasJavadoc(ast)) {
            log(ast, MSG_PKG_JAVADOC_MISSING);
        }
    }
