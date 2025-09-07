    private boolean canAssignValueToClassField(DetailAST ast) {
        final AbstractFrame fieldUsageFrame = findFrame(ast, false);
        final boolean fieldUsageInConstructor = isInsideConstructorFrame(fieldUsageFrame);

        final AbstractFrame declarationFrame = findFrame(ast, true);
        final boolean finalField = ((ClassFrame) declarationFrame).hasFinalField(ast);

        return fieldUsageInConstructor || !finalField;
    }
