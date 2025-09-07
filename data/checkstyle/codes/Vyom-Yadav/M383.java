    private static void extractInlineTagContent(DetailNode node,
        StringBuilder customTagContent) {
        final DetailNode[] children = node.getChildren();
        if (children.length == 0) {
            customTagContent.append(node.getText());
        }
        else {
            for (DetailNode child : children) {
                if (child.getType() != JavadocTokenTypes.LEADING_ASTERISK) {
                    extractInlineTagContent(child, customTagContent);
                }
            }
        }
    }
