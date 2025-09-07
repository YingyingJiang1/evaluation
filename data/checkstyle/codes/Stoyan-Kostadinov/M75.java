    public static String getDefaultValue(String propertyName, Field field,
                                         Object classInstance, String moduleName)
            throws MacroExecutionException {
        final Object value = getFieldValue(field, classInstance);
        final Class<?> fieldClass = getFieldClass(field, propertyName, moduleName, classInstance);
        String result = null;
        if (CHARSET.equals(propertyName)) {
            result = "the charset property of the parent"
                    + " <a href=\"https://checkstyle.org/config.html#Checker\">Checker</a> module";
        }
        else if (classInstance instanceof PropertyCacheFile) {
            result = "null (no cache file)";
        }
        else if (fieldClass == boolean.class) {
            result = value.toString();
        }
        else if (fieldClass == int.class) {
            result = value.toString();
        }
        else if (fieldClass == int[].class) {
            result = getIntArrayPropertyValue(value);
        }
        else if (fieldClass == double[].class) {
            result = removeSquareBrackets(Arrays.toString((double[]) value).replace(".0", ""));
            if (result.isEmpty()) {
                result = CURLY_BRACKETS;
            }
        }
        else if (fieldClass == String[].class) {
            result = getStringArrayPropertyValue(propertyName, value);
        }
        else if (fieldClass == URI.class || fieldClass == String.class) {
            if (value != null) {
                result = '"' + value.toString() + '"';
            }
        }
        else if (fieldClass == Pattern.class) {
            if (value != null) {
                result = '"' + value.toString().replace("\n", "\\n").replace("\t", "\\t")
                        .replace("\r", "\\r").replace("\f", "\\f") + '"';
            }
        }
        else if (fieldClass == Pattern[].class) {
            result = getPatternArrayPropertyValue(value);
        }
        else if (fieldClass.isEnum()) {
            if (value != null) {
                result = value.toString().toLowerCase(Locale.ENGLISH);
            }
        }
        else if (fieldClass == AccessModifierOption[].class) {
            result = removeSquareBrackets(Arrays.toString((Object[]) value));
        }
        else {
            final String message = String.format(Locale.ROOT,
                    "Unknown property type: %s", fieldClass.getSimpleName());
            throw new MacroExecutionException(message);
        }

        if (result == null) {
            result = "null";
        }

        return result;
    }
