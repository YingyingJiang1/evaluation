    @Override
    public DetailAstImpl visitElementValuePair(JavaLanguageParser.ElementValuePairContext ctx) {
        final DetailAstImpl elementValuePair =
                createImaginary(TokenTypes.ANNOTATION_MEMBER_VALUE_PAIR);
        processChildren(elementValuePair, ctx.children);
        return elementValuePair;
    }
