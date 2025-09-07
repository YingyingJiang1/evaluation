    @Override
    public void visitJavadocToken(DetailNode ast) {
        if (!isCommentOrInlineTag(ast.getParent())) {
            final Matcher tagMatcher = JAVADOC_BLOCK_TAG_PATTERN.matcher(ast.getText());
            while (tagMatcher.find()) {
                final String tagName = tagMatcher.group(1);
                if (tags.contains(tagName)) {
                    log(ast.getLineNumber(), MSG_BLOCK_TAG_LOCATION, tagName);
                }
            }
        }
    }
