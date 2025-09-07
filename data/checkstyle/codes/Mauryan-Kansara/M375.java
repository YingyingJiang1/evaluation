    @Nullable
    private static DetailNode findFirstHtmlElementAfter(DetailNode tag) {
        DetailNode htmlElement = getNextSibling(tag);

        while (htmlElement != null
                && htmlElement.getType() != JavadocTokenTypes.HTML_ELEMENT
                && htmlElement.getType() != JavadocTokenTypes.HTML_TAG) {
            if ((htmlElement.getType() == JavadocTokenTypes.TEXT
                    || htmlElement.getType() == JavadocTokenTypes.JAVADOC_INLINE_TAG)
                    && !CommonUtil.isBlank(htmlElement.getText())) {
                htmlElement = null;
                break;
            }
            htmlElement = JavadocUtil.getNextSibling(htmlElement);
        }

        return htmlElement;
    }
