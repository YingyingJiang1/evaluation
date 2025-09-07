    public static String getContentOfInlineCustomTag(DetailNode inlineTag) {
        final DetailNode[] childrenOfInlineTag = inlineTag.getChildren();
        final StringBuilder customTagContent = new StringBuilder(256);
        final int indexOfContentOfSummaryTag = 3;
        if (childrenOfInlineTag.length != indexOfContentOfSummaryTag) {
            DetailNode currentNode = childrenOfInlineTag[indexOfContentOfSummaryTag];
            while (currentNode.getType() != JavadocTokenTypes.JAVADOC_INLINE_TAG_END) {
                extractInlineTagContent(currentNode, customTagContent);
                currentNode = JavadocUtil.getNextSibling(currentNode);
            }
        }
        return customTagContent.toString();
    }
