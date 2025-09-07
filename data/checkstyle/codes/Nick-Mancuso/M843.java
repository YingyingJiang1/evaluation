    @Override
    public DetailAstImpl visitExpressionList(JavaLanguageParser.ExpressionListContext ctx) {
        final DetailAstImpl elist = createImaginary(TokenTypes.ELIST);
        processChildren(elist, ctx.children);
        return elist;
    }
