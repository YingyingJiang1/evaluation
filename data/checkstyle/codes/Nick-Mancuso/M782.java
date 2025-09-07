    @Override
    public DetailAstImpl visitArrayInitializer(JavaLanguageParser.ArrayInitializerContext ctx) {
        final DetailAstImpl arrayInitializer = create(TokenTypes.ARRAY_INIT, ctx.start);
        // ARRAY_INIT was child[0]
        processChildren(arrayInitializer, ctx.children.subList(1, ctx.children.size()));
        return arrayInitializer;
    }
