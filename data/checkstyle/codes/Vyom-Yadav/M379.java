    public static boolean isTextPresentInsideHtmlTag(DetailNode node) {
        DetailNode nestedChild = JavadocUtil.getFirstChild(node);
        if (node.getType() == JavadocTokenTypes.HTML_ELEMENT) {
            nestedChild = JavadocUtil.getFirstChild(nestedChild);
        }
        boolean isTextPresentInsideHtmlTag = false;
        while (nestedChild != null && !isTextPresentInsideHtmlTag) {
            switch (nestedChild.getType()) {
                case JavadocTokenTypes.TEXT:
                    isTextPresentInsideHtmlTag = !nestedChild.getText().isBlank();
                    break;
                case JavadocTokenTypes.HTML_TAG:
                case JavadocTokenTypes.HTML_ELEMENT:
                    isTextPresentInsideHtmlTag = isTextPresentInsideHtmlTag(nestedChild);
                    break;
                default:
                    break;
            }
            nestedChild = JavadocUtil.getNextSibling(nestedChild);
        }
        return isTextPresentInsideHtmlTag;
    }
