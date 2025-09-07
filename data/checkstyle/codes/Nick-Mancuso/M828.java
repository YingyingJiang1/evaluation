    @Override
    public DetailAstImpl visitCatchClause(JavaLanguageParser.CatchClauseContext ctx) {
        final DetailAstImpl catchClause = create(TokenTypes.LITERAL_CATCH,
                (Token) ctx.LITERAL_CATCH().getPayload());
        // 'LITERAL_CATCH' is child[0]
        processChildren(catchClause, ctx.children.subList(1, ctx.children.size()));
        return catchClause;
    }
