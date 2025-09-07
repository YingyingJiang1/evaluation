    @SuppressWarnings("deprecation")
    @Override
    public final void visitToken(DetailAST ast) {
        final Scope theScope = ScopeUtil.getScope(ast);
        if (shouldCheck(ast, theScope)) {
            final FileContents contents = getFileContents();
            final TextBlock textBlock = contents.getJavadocBefore(ast.getLineNo());

            if (textBlock == null && !isMissingJavadocAllowed(ast)) {
                log(ast, MSG_JAVADOC_MISSING);
            }
        }
    }
