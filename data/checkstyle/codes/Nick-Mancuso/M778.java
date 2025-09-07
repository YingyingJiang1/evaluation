    @Override
    public DetailAstImpl visitInterfaceBodyDeclaration(
            JavaLanguageParser.InterfaceBodyDeclarationContext ctx) {
        final DetailAstImpl returnTree;
        if (ctx.SEMI() == null) {
            returnTree = visit(ctx.interfaceMemberDeclaration());
        }
        else {
            returnTree = create(ctx.SEMI());
        }
        return returnTree;
    }
