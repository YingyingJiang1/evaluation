    @Override
    public DetailAstImpl visitTypeUpperBounds(JavaLanguageParser.TypeUpperBoundsContext ctx) {
        // In this case, we call 'extends` TYPE_UPPER_BOUNDS
        final DetailAstImpl typeUpperBounds = create(TokenTypes.TYPE_UPPER_BOUNDS,
                (Token) ctx.EXTENDS_CLAUSE().getPayload());
        // 'extends' is child[0]
        processChildren(typeUpperBounds, ctx.children.subList(1, ctx.children.size()));
        return typeUpperBounds;
    }
