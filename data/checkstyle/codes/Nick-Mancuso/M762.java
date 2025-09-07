    @Override
    public DetailAstImpl visitImplementsClause(JavaLanguageParser.ImplementsClauseContext ctx) {
        final DetailAstImpl classImplements = create(TokenTypes.IMPLEMENTS_CLAUSE,
                (Token) ctx.LITERAL_IMPLEMENTS().getPayload());
        classImplements.addChild(visit(ctx.typeList()));
        return classImplements;
    }
