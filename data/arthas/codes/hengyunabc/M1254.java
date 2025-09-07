    @Override
    public <T> T getRequiredProperty(String key, Class<T> valueType) throws IllegalStateException {
        T value = getProperty(key, valueType);
        if (value == null) {
            throw new IllegalStateException("Required key '" + key + "' not found");
        }
        return value;
    }
