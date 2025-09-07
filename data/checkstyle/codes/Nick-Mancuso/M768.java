    @Override
    public DetailAstImpl visitEnumConstant(JavaLanguageParser.EnumConstantContext ctx) {
        final DetailAstImpl enumConstant =
                createImaginary(TokenTypes.ENUM_CONSTANT_DEF);
        processChildren(enumConstant, ctx.children);
        return enumConstant;
    }
