    private static Optional<DetailNode> getPropertyJavadocNodeInModule(String propertyName,
                                                             DetailNode moduleJavadoc) {
        Optional<DetailNode> propertyJavadocNode = Optional.empty();

        for (DetailNode htmlElement = JavadocUtil.getNextSibling(
                JavadocUtil.getFirstChild(moduleJavadoc), JavadocTokenTypes.HTML_ELEMENT);
            htmlElement != null && propertyJavadocNode.isEmpty();
            htmlElement = JavadocUtil.getNextSibling(
                htmlElement, JavadocTokenTypes.HTML_ELEMENT)) {

            final DetailNode htmlTag = JavadocUtil.findFirstToken(
                htmlElement, JavadocTokenTypes.HTML_TAG);
            final Optional<String> htmlTagName = Optional.ofNullable(htmlTag)
                .map(JavadocUtil::getFirstChild)
                .map(htmlStart -> {
                    return JavadocUtil.findFirstToken(htmlStart, JavadocTokenTypes.HTML_TAG_NAME);
                })
                .map(DetailNode::getText);

            if (htmlTag != null && "ul".equals(htmlTagName.orElse(null))) {

                boolean foundProperty = false;

                for (DetailNode innerHtmlElement = JavadocUtil.getNextSibling(
                        JavadocUtil.getFirstChild(htmlTag), JavadocTokenTypes.HTML_ELEMENT);
                    innerHtmlElement != null && !foundProperty;
                    innerHtmlElement = JavadocUtil.getNextSibling(
                        innerHtmlElement, JavadocTokenTypes.HTML_ELEMENT)) {

                    final DetailNode liTag = JavadocUtil.getFirstChild(innerHtmlElement);

                    if (liTag.getType() == JavadocTokenTypes.LI) {

                        final DetailNode primeJavadocInlineTag = JavadocUtil.findFirstToken(liTag,
                            JavadocTokenTypes.JAVADOC_INLINE_TAG);

                        if (primeJavadocInlineTag == null) {
                            break;
                        }

                        final String examinedPropertyName = JavadocUtil.findFirstToken(
                            primeJavadocInlineTag, JavadocTokenTypes.TEXT).getText();

                        if (examinedPropertyName.equals(propertyName)) {
                            propertyJavadocNode = Optional.ofNullable(liTag);
                            foundProperty = true;
                        }
                    }
                }
            }
        }

        return propertyJavadocNode;
    }
