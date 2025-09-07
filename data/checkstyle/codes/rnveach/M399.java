    @Override
    public void visitToken(DetailAST ast) {
        final String commentContent = JavadocUtil.getBlockCommentContent(ast);

        if (JavadocUtil.isJavadocComment(commentContent)
                && !JavadocUtil.isCorrectJavadocPosition(ast)) {
            log(ast, MSG_KEY);
        }
    }
