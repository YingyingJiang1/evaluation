    @Override
    public DetailAstImpl visitInterfaceExtends(JavaLanguageParser.InterfaceExtendsContext ctx) {
        final DetailAstImpl interfaceExtends = create(ctx.EXTENDS_CLAUSE());
        interfaceExtends.addChild(visit(ctx.typeList()));
        return interfaceExtends;
    }
