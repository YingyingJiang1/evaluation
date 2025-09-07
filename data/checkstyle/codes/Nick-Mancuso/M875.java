    @Override
    public DetailAstImpl visitArrayDeclarator(JavaLanguageParser.ArrayDeclaratorContext ctx) {
        final DetailAstImpl arrayDeclarator = create(TokenTypes.ARRAY_DECLARATOR,
                (Token) ctx.LBRACK().getPayload());
        arrayDeclarator.addChild(create(ctx.RBRACK()));

        final DetailAstImpl returnTree;
        final DetailAstImpl annotations = visit(ctx.anno);
        if (annotations == null) {
            returnTree = arrayDeclarator;
        }
        else {
            returnTree = annotations;
            addLastSibling(returnTree, arrayDeclarator);
        }
        return returnTree;
    }
