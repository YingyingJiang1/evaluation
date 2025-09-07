    private static String getFieldNameFromSetterMethod(String methodName) {
        String field = methodName.substring("set".length());
        String startPart = field.substring(0, 1).toLowerCase();
        String endPart = field.substring(1);

        field = startPart + endPart;
        return field;
    }
