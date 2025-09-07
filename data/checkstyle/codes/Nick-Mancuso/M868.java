    @Override
    public DetailAstImpl visitCreatedNameObject(JavaLanguageParser.CreatedNameObjectContext ctx) {
        final DetailAstPair currentAST = new DetailAstPair();
        DetailAstPair.addAstChild(currentAST, visit(ctx.annotations()));
        DetailAstPair.addAstChild(currentAST, visit(ctx.id()));
        DetailAstPair.addAstChild(currentAST, visit(ctx.typeArgumentsOrDiamond()));

        // This is how we build the type arguments/ qualified name tree
        for (ParserRuleContext extendedContext : ctx.extended) {
            final DetailAstImpl dot = create(extendedContext.start);
            DetailAstPair.makeAstRoot(currentAST, dot);
            final List<ParseTree> childList = extendedContext
                    .children.subList(1, extendedContext.children.size());
            processChildren(dot, childList);
        }

        return currentAST.root;
    }
