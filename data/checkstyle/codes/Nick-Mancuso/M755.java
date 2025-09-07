    @Override
    public DetailAstImpl visitRecordComponentsList(
            JavaLanguageParser.RecordComponentsListContext ctx) {
        final DetailAstImpl lparen = create(ctx.LPAREN());

        // We make a "RECORD_COMPONENTS" node whether components exist or not
        if (ctx.recordComponents() == null) {
            addLastSibling(lparen, createImaginary(TokenTypes.RECORD_COMPONENTS));
        }
        else {
            addLastSibling(lparen, visit(ctx.recordComponents()));
        }
        addLastSibling(lparen, create(ctx.RPAREN()));
        return lparen;
    }
