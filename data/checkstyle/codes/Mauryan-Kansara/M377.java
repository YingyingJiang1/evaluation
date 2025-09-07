    private static DetailNode getNextSibling(DetailNode tag) {
        DetailNode nextSibling;

        if (JavadocUtil.getFirstChild(tag).getType() == JavadocTokenTypes.PARAGRAPH) {
            final DetailNode paragraphToken = JavadocUtil.getFirstChild(tag);
            final DetailNode paragraphStartTagToken = JavadocUtil.getFirstChild(paragraphToken);
            nextSibling = JavadocUtil.getNextSibling(paragraphStartTagToken);
        }
        else {
            nextSibling = JavadocUtil.getNextSibling(tag);
        }

        if (nextSibling.getType() == JavadocTokenTypes.HTML_COMMENT) {
            nextSibling = JavadocUtil.getNextSibling(nextSibling);
        }

        return nextSibling;
    }
