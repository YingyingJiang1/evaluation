    private static String convertUpperCamelToUpperUnderscore(String text) {
        final StringBuilder result = new StringBuilder(20);
        boolean first = true;
        for (char letter : text.toCharArray()) {
            if (!first && Character.isUpperCase(letter)) {
                result.append('_');
            }
            result.append(Character.toUpperCase(letter));
            first = false;
        }
        return result.toString();
    }
