    public static String[] split(String toSplit, String delimiter) {
        if(hasLength(toSplit) && hasLength(delimiter)) {
            int offset = toSplit.indexOf(delimiter);
            if(offset < 0) {
                return null;
            } else {
                String beforeDelimiter = toSplit.substring(0, offset);
                String afterDelimiter = toSplit.substring(offset + delimiter.length());
                return new String[]{beforeDelimiter, afterDelimiter};
            }
        } else {
            return null;
        }
    }
