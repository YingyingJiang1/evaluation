    private static String getPatternArrayPropertyValue(Object fieldValue) {
        Object value = fieldValue;
        if (value instanceof Collection) {
            final Collection<?> collection = (Collection<?>) value;

            value = collection.stream()
                    .map(Pattern.class::cast)
                    .toArray(Pattern[]::new);
        }

        String result = "";
        if (value != null && Array.getLength(value) > 0) {
            result = removeSquareBrackets(
                    Arrays.stream((Pattern[]) value)
                    .map(Pattern::pattern)
                    .collect(Collectors.joining(COMMA_SPACE)));
        }

        if (result.isEmpty()) {
            result = CURLY_BRACKETS;
        }
        return result;
    }
