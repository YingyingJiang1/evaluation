    private DetailAstImpl createTypeDeclaration(ParserRuleContext ctx, int type,
                                                List<? extends ParseTree> modifierList) {
        final DetailAstImpl typeDeclaration = createImaginary(type);
        typeDeclaration.addChild(createModifiers(modifierList));
        processChildren(typeDeclaration, ctx.children);
        return typeDeclaration;
    }
