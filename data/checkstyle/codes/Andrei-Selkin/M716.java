    private static Set<DetailAST> getAllTokensWhichAreEqualToCurrent(DetailAST ast, DetailAST token,
                                                                     int endLineNumber) {
        DetailAST vertex = ast;
        final Set<DetailAST> result = new HashSet<>();
        final Deque<DetailAST> stack = new ArrayDeque<>();
        while (vertex != null || !stack.isEmpty()) {
            if (!stack.isEmpty()) {
                vertex = stack.pop();
            }
            while (vertex != null) {
                if (isAstSimilar(token, vertex)
                        && vertex.getLineNo() <= endLineNumber) {
                    result.add(vertex);
                }
                if (vertex.getNextSibling() != null) {
                    stack.push(vertex.getNextSibling());
                }
                vertex = vertex.getFirstChild();
            }
        }
        return result;
    }
