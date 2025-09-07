    @Override
    public DetailAstImpl visitClassExtends(JavaLanguageParser.ClassExtendsContext ctx) {
        final DetailAstImpl classExtends = create(ctx.EXTENDS_CLAUSE());
        classExtends.addChild(visit(ctx.type));
        return classExtends;
    }
