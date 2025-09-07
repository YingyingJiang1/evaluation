    @Override
    public void visitToken(DetailAST ast) {
        final AccessModifierOption accessModifier =
            CheckUtil.getAccessModifierFromModifiersToken(ast);

        if (matchAccessModifiers(accessModifier)) {
            final DetailAST recordComponents =
                ast.findFirstToken(TokenTypes.RECORD_COMPONENTS);
            final int componentCount = countComponents(recordComponents);

            if (componentCount > max) {
                log(ast, MSG_KEY, componentCount, max);
            }
        }
    }
