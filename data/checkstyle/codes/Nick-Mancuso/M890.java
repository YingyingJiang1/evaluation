    private DetailAstImpl createModifiers(List<? extends ParseTree> modifierList) {
        final DetailAstImpl mods = createImaginary(TokenTypes.MODIFIERS);
        processChildren(mods, modifierList);
        return mods;
    }
