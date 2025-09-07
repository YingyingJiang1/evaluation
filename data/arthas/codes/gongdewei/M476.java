    private String renderItemValue(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Collection) {
            return renderCollectionValue((Collection) value);
        } else if (value instanceof String[]) {
            return renderArrayValue((String[]) value);
        } else if (value instanceof Map) {
            return renderMapValue((Map) value);
        }
        return String.valueOf(value);
    }
