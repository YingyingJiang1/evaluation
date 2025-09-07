    private static String getStringArrayPropertyValue(String propertyName, Object value) {
        String result;
        if (value == null) {
            result = "";
        }
        else {
            try (Stream<?> valuesStream = getValuesStream(value)) {
                result = valuesStream
                    .map(String.class::cast)
                    .sorted()
                    .collect(Collectors.joining(COMMA_SPACE));
            }
        }

        if (result.isEmpty()) {
            if (FILE_EXTENSIONS.equals(propertyName)) {
                result = "all files";
            }
            else {
                result = CURLY_BRACKETS;
            }
        }
        return result;
    }
