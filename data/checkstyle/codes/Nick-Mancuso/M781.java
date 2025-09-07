    @Override
    public DetailAstImpl visitVariableDeclaratorId(
            JavaLanguageParser.VariableDeclaratorIdContext ctx) {
        final DetailAstImpl root = new DetailAstImpl();
        root.addChild(createModifiers(ctx.mods));
        final DetailAstImpl type = visit(ctx.type);
        root.addChild(type);

        final DetailAstImpl declaratorId;
        if (ctx.LITERAL_THIS() == null) {
            declaratorId = visit(ctx.qualifiedName());
        }
        else if (ctx.DOT() == null) {
            declaratorId = create(ctx.LITERAL_THIS());
        }
        else {
            declaratorId = create(ctx.DOT());
            declaratorId.addChild(visit(ctx.qualifiedName()));
            declaratorId.addChild(create(ctx.LITERAL_THIS()));
        }

        root.addChild(declaratorId);
        ctx.arrayDeclarator().forEach(child -> type.addChild(visit(child)));

        return root.getFirstChild();
    }
