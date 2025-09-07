    @Override
    public DetailAstImpl visitEnhancedFor(JavaLanguageParser.EnhancedForContext ctx) {
        final DetailAstImpl leftParen = create(ctx.LPAREN());
        final DetailAstImpl enhancedForControl =
                 visit(ctx.getChild(1));
        final DetailAstImpl forEachClause = createImaginary(TokenTypes.FOR_EACH_CLAUSE);
        forEachClause.addChild(enhancedForControl);
        addLastSibling(leftParen, forEachClause);
        addLastSibling(leftParen, create(ctx.RPAREN()));
        return leftParen;
    }
