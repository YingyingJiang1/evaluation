    @Override
    public DetailAstImpl visitClassOrInterfaceType(
            JavaLanguageParser.ClassOrInterfaceTypeContext ctx) {
        final DetailAstPair currentAST = new DetailAstPair();
        DetailAstPair.addAstChild(currentAST, visit(ctx.id()));
        DetailAstPair.addAstChild(currentAST, visit(ctx.typeArguments()));

        // This is how we build the annotations/ qualified name/ type parameters tree
        for (ParserRuleContext extendedContext : ctx.extended) {
            final DetailAstImpl dot = create(extendedContext.start);
            DetailAstPair.makeAstRoot(currentAST, dot);
            extendedContext.children
                .forEach(child -> DetailAstPair.addAstChild(currentAST, visit(child)));
        }

        // Create imaginary 'TYPE' parent if specified
        final DetailAstImpl returnTree;
        if (ctx.createImaginaryNode) {
            returnTree = createImaginary(TokenTypes.TYPE);
            returnTree.addChild(currentAST.root);
        }
        else {
            returnTree = currentAST.root;
        }
        return returnTree;
    }
