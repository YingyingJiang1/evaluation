    @SuppressWarnings("unchecked")
    @Override
    public T convert(String source, Class<T> targetType) {
        return (T) Enum.valueOf(targetType, source);
    }
