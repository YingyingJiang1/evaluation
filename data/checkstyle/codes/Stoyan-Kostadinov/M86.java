        private static String getDescriptionFromJavadoc(DetailNode javadoc, String moduleName)
                throws MacroExecutionException {
            boolean isInCodeLiteral = false;
            boolean isInHtmlElement = false;
            boolean isInHrefAttribute = false;
            final StringBuilder description = new StringBuilder(128);
            final Deque<DetailNode> queue = new ArrayDeque<>();
            final List<DetailNode> descriptionNodes = getDescriptionNodes(javadoc);
            Lists.reverse(descriptionNodes).forEach(queue::push);

            // Perform DFS traversal on description nodes
            while (!queue.isEmpty()) {
                final DetailNode node = queue.pop();
                Lists.reverse(Arrays.asList(node.getChildren())).forEach(queue::push);

                if (node.getType() == JavadocTokenTypes.HTML_TAG_NAME
                        && "href".equals(node.getText())) {
                    isInHrefAttribute = true;
                }
                if (isInHrefAttribute && node.getType() == JavadocTokenTypes.ATTR_VALUE) {
                    final String href = node.getText();
                    if (href.contains(CHECKSTYLE_ORG_URL)) {
                        handleInternalLink(description, moduleName, href);
                    }
                    else {
                        description.append(href);
                    }

                    isInHrefAttribute = false;
                    continue;
                }
                if (node.getType() == JavadocTokenTypes.HTML_ELEMENT) {
                    isInHtmlElement = true;
                }
                if (node.getType() == JavadocTokenTypes.END
                        && node.getParent().getType() == JavadocTokenTypes.HTML_ELEMENT_END) {
                    description.append(node.getText());
                    isInHtmlElement = false;
                }
                if (node.getType() == JavadocTokenTypes.TEXT
                        // If a node has children, its text is not part of the description
                        || isInHtmlElement && node.getChildren().length == 0
                            // Some HTML elements span multiple lines, so we avoid the asterisk
                            && node.getType() != JavadocTokenTypes.LEADING_ASTERISK) {
                    description.append(node.getText());
                }
                if (node.getType() == JavadocTokenTypes.CODE_LITERAL) {
                    isInCodeLiteral = true;
                    description.append("<code>");
                }
                if (isInCodeLiteral
                        && node.getType() == JavadocTokenTypes.JAVADOC_INLINE_TAG_END) {
                    isInCodeLiteral = false;
                    description.append("</code>");
                }
            }
            return description.toString().trim();
        }
