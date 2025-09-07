    @Override
    public DetailAstImpl visitTypeParameter(JavaLanguageParser.TypeParameterContext ctx) {
        final DetailAstImpl typeParameter = createImaginary(TokenTypes.TYPE_PARAMETER);
        processChildren(typeParameter, ctx.children);
        return typeParameter;
    }
