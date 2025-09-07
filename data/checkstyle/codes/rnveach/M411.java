    @Override
    public void init() {
        validateDefaultJavadocTokens();
        if (javadocTokens.isEmpty()) {
            javadocTokens.addAll(
                    Arrays.stream(getDefaultJavadocTokens()).boxed()
                        .collect(Collectors.toUnmodifiableList()));
        }
        else {
            final int[] acceptableJavadocTokens = getAcceptableJavadocTokens();
            Arrays.sort(acceptableJavadocTokens);
            for (Integer javadocTokenId : javadocTokens) {
                if (Arrays.binarySearch(acceptableJavadocTokens, javadocTokenId) < 0) {
                    final String message = String.format(Locale.ROOT, "Javadoc Token \"%s\" was "
                            + "not found in Acceptable javadoc tokens list in check %s",
                            JavadocUtil.getTokenName(javadocTokenId), getClass().getName());
                    throw new IllegalStateException(message);
                }
            }
        }
    }
