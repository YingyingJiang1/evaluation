    private static boolean containsInheritDocTag(DetailAST ast) {
        final DetailAST modifiers = ast.getFirstChild();
        final DetailAST startNode;
        if (modifiers.hasChildren()) {
            startNode = Optional.ofNullable(ast.getFirstChild()
                    .findFirstToken(TokenTypes.ANNOTATION))
                .orElse(modifiers);
        }
        else {
            startNode = ast.findFirstToken(TokenTypes.TYPE);
        }
        final Optional<String> javadoc =
            Stream.iterate(startNode.getLastChild(), Objects::nonNull,
                    DetailAST::getPreviousSibling)
            .filter(node -> node.getType() == TokenTypes.BLOCK_COMMENT_BEGIN)
            .map(DetailAST::getFirstChild)
            .map(DetailAST::getText)
            .filter(JavadocUtil::isJavadocComment)
            .findFirst();
        return javadoc.isPresent()
                && MATCH_INHERIT_DOC.matcher(javadoc.orElseThrow()).find();
    }
