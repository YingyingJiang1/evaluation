    @Override
    public DetailAstImpl visitImportDec(JavaLanguageParser.ImportDecContext ctx) {
        final DetailAstImpl importRoot = create(ctx.start);

        // Static import
        final TerminalNode literalStaticNode = ctx.LITERAL_STATIC();
        if (literalStaticNode != null) {
            importRoot.setType(TokenTypes.STATIC_IMPORT);
            importRoot.addChild(create(literalStaticNode));
        }

        // Handle star imports
        final boolean isStarImport = ctx.STAR() != null;
        if (isStarImport) {
            final DetailAstImpl dot = create(ctx.DOT());
            dot.addChild(visit(ctx.qualifiedName()));
            dot.addChild(create(ctx.STAR()));
            importRoot.addChild(dot);
        }
        else {
            importRoot.addChild(visit(ctx.qualifiedName()));
        }

        importRoot.addChild(create(ctx.SEMI()));
        return importRoot;
    }
