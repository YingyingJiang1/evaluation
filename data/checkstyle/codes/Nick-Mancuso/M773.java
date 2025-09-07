    @Override
    public DetailAstImpl visitClassBlock(JavaLanguageParser.ClassBlockContext ctx) {
        final DetailAstImpl classBlock;
        if (ctx.LITERAL_STATIC() == null) {
            // We call it an INSTANCE_INIT
            classBlock = createImaginary(TokenTypes.INSTANCE_INIT);
        }
        else {
            classBlock = create(TokenTypes.STATIC_INIT, (Token) ctx.LITERAL_STATIC().getPayload());
            classBlock.setText(TokenUtil.getTokenName(TokenTypes.STATIC_INIT));
        }
        classBlock.addChild(visit(ctx.block()));
        return classBlock;
    }
