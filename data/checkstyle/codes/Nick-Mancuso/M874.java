    @Override
    public DetailAstImpl visitTypeType(JavaLanguageParser.TypeTypeContext ctx) {
        final DetailAstImpl type = createImaginary(TokenTypes.TYPE);
        processChildren(type, ctx.children);

        final DetailAstImpl returnTree;
        if (ctx.createImaginaryNode) {
            returnTree = type;
        }
        else {
            returnTree = type.getFirstChild();
        }
        return returnTree;
    }
