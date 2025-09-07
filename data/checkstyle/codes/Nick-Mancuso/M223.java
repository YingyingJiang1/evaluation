    private static int lastIndexOfNonWhitespace(String line) {
        int length;
        for (length = line.length(); length > 0; length--) {
            if (!Character.isWhitespace(line.charAt(length - 1))) {
                break;
            }
        }
        return length;
    }
