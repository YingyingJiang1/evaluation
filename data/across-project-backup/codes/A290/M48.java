    @Override
    public boolean ignore(String fieldName, Integer columnIndex) {
        if (fieldName != null) {
            if (includeColumnFieldNames != null && !includeColumnFieldNames.contains(fieldName)) {
                return true;
            }
            if (excludeColumnFieldNames != null && excludeColumnFieldNames.contains(fieldName)) {
                return true;
            }
        }
        if (columnIndex != null) {
            if (includeColumnIndexes != null && !includeColumnIndexes.contains(columnIndex)) {
                return true;
            }
            if (excludeColumnIndexes != null && excludeColumnIndexes.contains(columnIndex)) {
                return true;
            }
        }
        return false;
    }
