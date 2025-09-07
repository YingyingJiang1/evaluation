    public static String after(String text, String after) {
        int pos = text.indexOf(after);
        if (pos == -1) {
            return null;
        }
        return text.substring(pos + after.length());
    }
