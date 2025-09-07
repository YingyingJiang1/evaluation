    private static String getIntArrayPropertyValue(Object value) {
        try (IntStream stream = getIntStream(value)) {
            String result = stream
                    .mapToObj(TokenUtil::getTokenName)
                    .sorted()
                    .collect(Collectors.joining(COMMA_SPACE));
            if (result.isEmpty()) {
                result = CURLY_BRACKETS;
            }
            return result;
        }
    }
