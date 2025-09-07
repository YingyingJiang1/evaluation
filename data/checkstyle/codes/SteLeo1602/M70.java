    private static Optional<String> getSpecifiedPropertyVersion(String propertyName,
                                                      DetailNode moduleJavadoc)
            throws MacroExecutionException {
        Optional<String> specifiedVersion = Optional.empty();

        final Optional<DetailNode> propertyModuleJavadoc =
            getPropertyJavadocNodeInModule(propertyName, moduleJavadoc);

        if (propertyModuleJavadoc.isPresent()) {
            final DetailNode primaryJavadocInlineTag = JavadocUtil.findFirstToken(
                propertyModuleJavadoc.get(), JavadocTokenTypes.JAVADOC_INLINE_TAG);

            for (DetailNode textNode = JavadocUtil
                .getNextSibling(primaryJavadocInlineTag, JavadocTokenTypes.TEXT);
                 textNode != null && specifiedVersion.isEmpty();
                 textNode = JavadocUtil.getNextSibling(
                     textNode, JavadocTokenTypes.TEXT)) {

                final String textNodeText = textNode.getText();

                if (textNodeText.startsWith(WHITESPACE + SINCE_VERSION)) {
                    final int sinceVersionIndex = textNodeText.indexOf('.') - 1;

                    if (sinceVersionIndex > 0) {
                        specifiedVersion = Optional.of(textNodeText.substring(sinceVersionIndex));
                    }
                    else {
                        throw new MacroExecutionException(textNodeText
                            + " has no valid version, at least one '.' is expected.");
                    }

                }
            }
        }

        return specifiedVersion;
    }
