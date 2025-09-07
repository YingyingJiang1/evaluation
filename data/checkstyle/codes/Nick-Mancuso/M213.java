    private static String getPropertyNameFromExpression(String variableExpression) {
        final int propertyStartIndex = variableExpression.lastIndexOf('{') + 1;
        final int propertyEndIndex = variableExpression.lastIndexOf('}');
        return variableExpression.substring(propertyStartIndex, propertyEndIndex);
    }
