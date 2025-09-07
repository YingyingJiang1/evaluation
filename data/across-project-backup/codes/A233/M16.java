    public static String safeToString(Object obj, int maxLength) {
        if (obj == null) {
            return "null";
        }

        String result;
        try {
            // Handle common types directly to avoid toString() overhead
            if (obj instanceof String) {
                result = (String) obj;
            } else if (obj instanceof Number || obj instanceof Boolean) {
                result = obj.toString();
            } else if (obj instanceof byte[]) {
                result = "[binary data length=" + ((byte[]) obj).length + "]";
            } else {
                // For complex objects, use toString but handle exceptions
                result = obj.toString();
            }

            // Truncate if necessary
            if (result != null && result.length() > maxLength) {
                return StringUtils.truncate(result, maxLength - 3) + "...";
            }

            return result;
        } catch (Exception e) {
            // If toString() fails, return the class name
            return "[" + obj.getClass().getName() + " - toString() failed]";
        }
    }
