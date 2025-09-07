    public static Optional<DetailAST> findFirstTokenByPredicate(DetailAST root,
                                                                Predicate<DetailAST> predicate) {
        Optional<DetailAST> result = Optional.empty();
        for (DetailAST ast = root.getFirstChild(); ast != null; ast = ast.getNextSibling()) {
            if (predicate.test(ast)) {
                result = Optional.of(ast);
                break;
            }
        }
        return result;
    }
