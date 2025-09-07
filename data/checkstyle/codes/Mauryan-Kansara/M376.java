    @Nullable
    private static String getHtmlElementName(DetailNode htmlElement) {
        final DetailNode htmlTag;
        if (htmlElement.getType() == JavadocTokenTypes.HTML_TAG) {
            htmlTag = htmlElement;
        }
        else {
            htmlTag = JavadocUtil.getFirstChild(htmlElement);
        }
        final DetailNode htmlTagFirstChild = JavadocUtil.getFirstChild(htmlTag);
        final DetailNode htmlTagName =
                JavadocUtil.findFirstToken(htmlTagFirstChild, JavadocTokenTypes.HTML_TAG_NAME);
        String blockTagName = null;
        if (htmlTagName != null && BLOCK_TAGS.contains(htmlTagName.getText())) {
            blockTagName = htmlTagName.getText();
        }

        return blockTagName;
    }
