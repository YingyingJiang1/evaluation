    private static String getStringOrDefault(Pattern pattern, String defaultString) {
        final String result;

        if (pattern == null) {
            result = defaultString;
        }
        else {
            result = pattern.toString();
        }

        return result;
    }
