    @Override
    public DetailAstImpl visitClassRefPrimary(JavaLanguageParser.ClassRefPrimaryContext ctx) {
        final DetailAstImpl dot = create(ctx.DOT());
        final DetailAstImpl primaryTypeNoArray = visit(ctx.type);
        dot.addChild(primaryTypeNoArray);
        if (TokenUtil.isOfType(primaryTypeNoArray, TokenTypes.DOT)) {
            // We append '[]' to the qualified name 'TYPE' `ast
            ctx.arrayDeclarator()
                    .forEach(child -> primaryTypeNoArray.addChild(visit(child)));
        }
        else {
            ctx.arrayDeclarator()
                    .forEach(child -> addLastSibling(primaryTypeNoArray, visit(child)));
        }
        dot.addChild(create(ctx.LITERAL_CLASS()));
        return dot;
    }
