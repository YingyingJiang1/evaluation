    private static int findIndexOfFirstNonBlankLine(String commentContent) {
        int lineNo = 0;
        boolean noContent = true;
        for (int i = 0; i < commentContent.length(); ++i) {
            final char character = commentContent.charAt(i);
            if (character == '\n') {
                ++lineNo;
            }
            else if (character != '*' && !Character.isWhitespace(character)) {
                noContent = false;
                break;
            }
        }
        if (noContent) {
            lineNo = -1;
        }
        return lineNo;
    }
