    @Override
    public DetailAstImpl visitBinOp(JavaLanguageParser.BinOpContext ctx) {
        final DetailAstImpl bop = create(ctx.bop);

        // To improve performance, we iterate through binary operations
        // since they are frequently deeply nested.
        final List<JavaLanguageParser.BinOpContext> binOpList = new ArrayList<>();
        ParseTree firstExpression = ctx.expr(0);
        while (firstExpression instanceof JavaLanguageParser.BinOpContext) {
            // Get all nested binOps
            binOpList.add((JavaLanguageParser.BinOpContext) firstExpression);
            firstExpression = ((JavaLanguageParser.BinOpContext) firstExpression).expr(0);
        }

        if (binOpList.isEmpty()) {
            final DetailAstImpl leftChild = visit(ctx.children.get(0));
            bop.addChild(leftChild);
        }
        else {
            // Map all descendants to individual AST's since we can parallelize this
            // operation
            final Queue<DetailAstImpl> descendantList = binOpList.parallelStream()
                    .map(this::getInnerBopAst)
                    .collect(Collectors.toCollection(ArrayDeque::new));

            bop.addChild(descendantList.poll());
            DetailAstImpl pointer = bop.getFirstChild();
            // Build tree
            for (DetailAstImpl descendant : descendantList) {
                pointer.getFirstChild().addPreviousSibling(descendant);
                pointer = descendant;
            }
        }

        bop.addChild(visit(ctx.children.get(2)));
        return bop;
    }
