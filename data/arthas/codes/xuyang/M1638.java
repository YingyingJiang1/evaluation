    private static BasicValue createBasicValue(Object value) {
        BasicValue.Builder builder = BasicValue.newBuilder();

        if (value instanceof Integer) {
            builder.setInt((int) value);
        } else if (value instanceof Long) {
            builder.setLong((long) value);
        } else if (value instanceof Float) {
            builder.setFloat((float) value);
        } else if (value instanceof Double) {
            builder.setDouble((double) value);
        } else if (value instanceof Boolean) {
            builder.setBoolean((boolean) value);
        } else if (value instanceof String) {
            builder.setString((String) value);
        }

        return builder.build();
    }
