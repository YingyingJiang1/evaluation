    @Override
    public DetailAstImpl visitSuperSuffixDot(JavaLanguageParser.SuperSuffixDotContext ctx) {
        final DetailAstImpl root;
        if (ctx.LPAREN() == null) {
            root = create(ctx.DOT());
            root.addChild(visit(ctx.id()));
        }
        else {
            root = create(TokenTypes.METHOD_CALL, (Token) ctx.LPAREN().getPayload());

            final DetailAstImpl dot = create(ctx.DOT());
            dot.addChild(visit(ctx.id()));
            root.addChild(dot);

            final DetailAstImpl expressionList = Optional.ofNullable(visit(ctx.expressionList()))
                    .orElseGet(() -> createImaginary(TokenTypes.ELIST));
            root.addChild(expressionList);

            root.addChild(create(ctx.RPAREN()));
        }

        return root;
    }
