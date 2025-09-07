    private String escapeEncode(final String string) {
        final StringBuilder returnSB = new StringBuilder();
        for (final char c : string.toCharArray()) {
            if (isIn(c, kvSegmentSeparator, kvSeparator, ESCAPE_PREFIX_CHAR)) {
                returnSB.append(ESCAPE_PREFIX_CHAR);
            }
            returnSB.append(c);
        }

        return returnSB.toString();
    }
