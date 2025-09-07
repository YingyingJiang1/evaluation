    public int[] getAcceptableJavadocTokens() {
        final int[] defaultJavadocTokens = getDefaultJavadocTokens();
        final int[] copy = new int[defaultJavadocTokens.length];
        System.arraycopy(defaultJavadocTokens, 0, copy, 0, defaultJavadocTokens.length);
        return copy;
    }
