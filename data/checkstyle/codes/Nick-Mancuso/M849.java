    @Override
    public DetailAstImpl visitCastExp(JavaLanguageParser.CastExpContext ctx) {
        final DetailAstImpl cast = create(TokenTypes.TYPECAST, (Token) ctx.LPAREN().getPayload());
        // child [0] is LPAREN
        processChildren(cast, ctx.children.subList(1, ctx.children.size()));
        return cast;
    }
