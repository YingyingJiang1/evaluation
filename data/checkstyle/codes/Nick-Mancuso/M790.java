    @Override
    public DetailAstImpl visitQualifiedName(JavaLanguageParser.QualifiedNameContext ctx) {
        final DetailAstImpl ast = visit(ctx.id());
        final DetailAstPair currentAst = new DetailAstPair();
        DetailAstPair.addAstChild(currentAst, ast);

        for (ParserRuleContext extendedContext : ctx.extended) {
            final DetailAstImpl dot = create(extendedContext.start);
            DetailAstPair.makeAstRoot(currentAst, dot);
            final List<ParseTree> childList = extendedContext
                    .children.subList(1, extendedContext.children.size());
            processChildren(dot, childList);
        }
        return currentAst.getRoot();
    }
