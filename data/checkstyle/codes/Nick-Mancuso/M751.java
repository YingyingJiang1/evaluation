    @Override
    public DetailAstImpl visitCompilationUnit(JavaLanguageParser.CompilationUnitContext ctx) {
        final DetailAstImpl compilationUnit;
        // 'EOF' token is always present; therefore if we only have one child, we have an empty file
        final boolean isEmptyFile = ctx.children.size() == 1;
        if (isEmptyFile) {
            compilationUnit = null;
        }
        else {
            compilationUnit = createImaginary(TokenTypes.COMPILATION_UNIT);
            // last child is 'EOF', we do not include this token in AST
            processChildren(compilationUnit, ctx.children.subList(0, ctx.children.size() - 1));
        }
        return compilationUnit;
    }
