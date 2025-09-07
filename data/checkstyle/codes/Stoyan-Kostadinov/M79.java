    private static Stream<?> getValuesStream(Object value) {
        final Stream<?> valuesStream;
        if (value instanceof Collection) {
            final Collection<?> collection = (Collection<?>) value;
            valuesStream = collection.stream();
        }
        else {
            final Object[] array = (Object[]) value;
            valuesStream = Arrays.stream(array);
        }
        return valuesStream;
    }
