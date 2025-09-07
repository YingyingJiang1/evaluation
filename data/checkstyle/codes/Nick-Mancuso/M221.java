    private static String stripIndentAndTrailingWhitespaceFromLine(String line, int indent) {
        final int lastNonWhitespace = lastIndexOfNonWhitespace(line);
        String returnString = "";
        if (lastNonWhitespace > 0) {
            returnString = line.substring(indent, lastNonWhitespace);
        }
        return returnString;
    }
