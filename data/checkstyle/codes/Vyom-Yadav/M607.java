    private static boolean isPrivateInstanceVariable(DetailAST varDefAst) {
        final AccessModifierOption varAccessModifier =
                CheckUtil.getAccessModifierFromModifiersToken(varDefAst);
        return varAccessModifier == AccessModifierOption.PRIVATE;
    }
