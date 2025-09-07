    @Override
    public DetailAstImpl visitInvOp(JavaLanguageParser.InvOpContext ctx) {
        final DetailAstPair currentAst = new DetailAstPair();

        final DetailAstImpl returnAst = visit(ctx.expr());
        DetailAstPair.addAstChild(currentAst, returnAst);
        DetailAstPair.makeAstRoot(currentAst, create(ctx.bop));

        DetailAstPair.addAstChild(currentAst,
                 visit(ctx.nonWildcardTypeArguments()));
        DetailAstPair.addAstChild(currentAst, visit(ctx.id()));
        final DetailAstImpl lparen = create(TokenTypes.METHOD_CALL,
                (Token) ctx.LPAREN().getPayload());
        DetailAstPair.makeAstRoot(currentAst, lparen);

        // We always add an 'ELIST' node
        final DetailAstImpl expressionList = Optional.ofNullable(visit(ctx.expressionList()))
                .orElseGet(() -> createImaginary(TokenTypes.ELIST));

        DetailAstPair.addAstChild(currentAst, expressionList);
        DetailAstPair.addAstChild(currentAst, create(ctx.RPAREN()));

        return currentAst.root;
    }
