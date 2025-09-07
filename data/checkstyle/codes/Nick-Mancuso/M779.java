    @Override
    public DetailAstImpl visitInterfaceMethodDeclaration(
            JavaLanguageParser.InterfaceMethodDeclarationContext ctx) {
        final DetailAstImpl methodDef = createImaginary(TokenTypes.METHOD_DEF);
        methodDef.addChild(createModifiers(ctx.mods));

        // Process all children except C style array declarators and modifiers
        final List<ParseTree> children = ctx.children
                .stream()
                .filter(child -> !(child instanceof JavaLanguageParser.ArrayDeclaratorContext))
                .collect(Collectors.toUnmodifiableList());
        processChildren(methodDef, children);

        // We add C style array declarator brackets to TYPE ast
        final DetailAstImpl typeAst = (DetailAstImpl) methodDef.findFirstToken(TokenTypes.TYPE);
        ctx.cStyleArrDec.forEach(child -> typeAst.addChild(visit(child)));

        return methodDef;
    }
