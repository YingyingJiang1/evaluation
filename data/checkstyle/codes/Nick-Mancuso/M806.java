    @Override
    public DetailAstImpl visitPrimaryCtorCall(JavaLanguageParser.PrimaryCtorCallContext ctx) {
        final DetailAstImpl primaryCtorCall = create(TokenTypes.SUPER_CTOR_CALL,
                (Token) ctx.LITERAL_SUPER().getPayload());
        // filter 'LITERAL_SUPER'
        processChildren(primaryCtorCall, ctx.children.stream()
                   .filter(child -> !child.equals(ctx.LITERAL_SUPER()))
                   .collect(Collectors.toUnmodifiableList()));
        return primaryCtorCall;
    }
