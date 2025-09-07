    @Override
    public int[] getDefaultJavadocTokens() {
        return new int[] {
            JavadocTokenTypes.JAVADOC,
            JavadocTokenTypes.PARAGRAPH,
            JavadocTokenTypes.LI,
            JavadocTokenTypes.SINCE_LITERAL,
        };
    }
