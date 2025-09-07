    private static Optional<DetailAST> getReferencedClassDot(DetailAST leftBracket) {
        final DetailAST parent = leftBracket.getParent();
        Optional<DetailAST> classDot = Optional.empty();
        if (parent.getType() != TokenTypes.ASSIGN) {
            classDot = Optional.ofNullable(parent.findFirstToken(TokenTypes.DOT));
        }
        return classDot;
    }
