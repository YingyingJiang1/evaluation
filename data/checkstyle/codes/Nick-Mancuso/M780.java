    @Override
    public DetailAstImpl visitVariableDeclarator(
            JavaLanguageParser.VariableDeclaratorContext ctx) {
        final DetailAstImpl variableDef = createImaginary(TokenTypes.VARIABLE_DEF);
        variableDef.addChild(createModifiers(ctx.mods));

        final DetailAstImpl type = visit(ctx.type);
        variableDef.addChild(type);
        variableDef.addChild(visit(ctx.id()));

        // Add C style array declarator brackets to TYPE ast
        ctx.arrayDeclarator().forEach(child -> type.addChild(visit(child)));

        // If this is an assignment statement, ASSIGN becomes the parent of EXPR
        final TerminalNode assignNode = ctx.ASSIGN();
        if (assignNode != null) {
            final DetailAstImpl assign = create(assignNode);
            variableDef.addChild(assign);
            assign.addChild(visit(ctx.variableInitializer()));
        }
        return variableDef;
    }
