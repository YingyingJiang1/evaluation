    private boolean hasValidJavadocComment(DetailAST detailAST) {
        final String javadocString =
            JavadocUtil.getBlockCommentContent(detailAST);

        final Matcher requiredJavadocPhraseMatcher =
            requiredJavadocPhrase.matcher(javadocString);

        return requiredJavadocPhraseMatcher.find();
    }
