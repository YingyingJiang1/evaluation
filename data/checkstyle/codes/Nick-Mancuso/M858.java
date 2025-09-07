    @Override
    public DetailAstImpl visitTernaryOp(JavaLanguageParser.TernaryOpContext ctx) {
        final DetailAstImpl root = create(ctx.QUESTION());
        processChildren(root, ctx.children.stream()
                .filter(child -> !child.equals(ctx.QUESTION()))
                .collect(Collectors.toUnmodifiableList()));
        return root;
    }
