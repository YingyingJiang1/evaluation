    private boolean lineInsideBlockComment(int lineNo) {
        final Collection<List<TextBlock>> values = clangComments.values();
        return values.stream()
            .flatMap(List::stream)
            .filter(comment -> !javadocComments.containsValue(comment))
            .anyMatch(comment -> {
                final boolean lineInSideBlockComment = lineNo >= comment.getStartLineNo()
                                                    && lineNo <= comment.getEndLineNo();
                boolean lineHasOnlyBlockComment = true;
                if (comment.getStartLineNo() == comment.getEndLineNo()) {
                    final String line = line(comment.getStartLineNo() - 1).trim();
                    lineHasOnlyBlockComment = line.startsWith("/*") && line.endsWith("*/");
                }
                return lineInSideBlockComment && lineHasOnlyBlockComment;
            });
    }
