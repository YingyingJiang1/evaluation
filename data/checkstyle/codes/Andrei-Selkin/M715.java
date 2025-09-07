    private static Set<DetailAST> getAllTokensOfType(DetailAST ast, int tokenType,
                                                     int endLineNumber) {
        DetailAST vertex = ast;
        final Set<DetailAST> result = new HashSet<>();
        final Deque<DetailAST> stack = new ArrayDeque<>();
        while (vertex != null || !stack.isEmpty()) {
            if (!stack.isEmpty()) {
                vertex = stack.pop();
            }
            while (vertex != null) {
                if (tokenType == vertex.getType()
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
