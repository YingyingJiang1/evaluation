    @Override
    public DetailAstImpl visitIndexOp(JavaLanguageParser.IndexOpContext ctx) {
        // LBRACK -> INDEX_OP is root of this AST
        final DetailAstImpl indexOp = create(TokenTypes.INDEX_OP,
                (Token) ctx.LBRACK().getPayload());

        // add expression(IDENT) on LHS
        indexOp.addChild(visit(ctx.expr(0)));

        // create imaginary node for expression on RHS
        final DetailAstImpl expr = visit(ctx.expr(1));
        final DetailAstImpl imaginaryExpr = createImaginary(TokenTypes.EXPR);
        imaginaryExpr.addChild(expr);
        indexOp.addChild(imaginaryExpr);

        // complete AST by adding RBRACK
        indexOp.addChild(create(ctx.RBRACK()));
        return indexOp;
    }
