        public void visitType(DetailAST ast) {
            DetailAST child = ast.getFirstChild();
            while (child != null) {
                if (TokenUtil.isOfType(child, TokenTypes.IDENT, TokenTypes.DOT)) {
                    final String fullTypeName = FullIdent.createFullIdent(child).getText();
                    final String trimmed = BRACKET_PATTERN
                            .matcher(fullTypeName).replaceAll("");
                    addReferencedClassName(trimmed);
                }
                child = child.getNextSibling();
            }
        }
