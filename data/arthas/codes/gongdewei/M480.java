    private String renderMemoryUsage(Map<String, Object> valueMap) {
        final StringBuilder colSB = new StringBuilder();
        String[] keys = new String[]{"init", "used", "committed", "max"};
        for (String key : keys) {
            Object value = valueMap.get(key);
            String valueStr = value != null ? formatMemoryByte((Long) value) : "";
            colSB.append(key).append(" : ").append(valueStr).append("\n");
        }
        return colSB.toString();
    }
