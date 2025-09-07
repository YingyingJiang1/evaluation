    private static DetailNode getSinceJavadocTag(DetailNode javadoc) {
        final DetailNode[] children = javadoc.getChildren();
        DetailNode javadocTagWithSince = null;
        for (final DetailNode child : children) {
            if (child.getType() == JavadocTokenTypes.JAVADOC_TAG) {
                final DetailNode sinceNode = JavadocUtil.findFirstToken(
                        child, JavadocTokenTypes.SINCE_LITERAL);
                if (sinceNode != null) {
                    javadocTagWithSince = child;
                    break;
                }
            }
        }
        return javadocTagWithSince;
    }
