    @Override
    public DetailAstImpl visitMethodRef(JavaLanguageParser.MethodRefContext ctx) {
        final DetailAstImpl doubleColon = create(TokenTypes.METHOD_REF,
                (Token) ctx.DOUBLE_COLON().getPayload());
        final List<ParseTree> children = ctx.children.stream()
                .filter(child -> !child.equals(ctx.DOUBLE_COLON()))
                .collect(Collectors.toUnmodifiableList());
        processChildren(doubleColon, children);
        return doubleColon;
    }
