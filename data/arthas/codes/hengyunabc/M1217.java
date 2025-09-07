    @Override
    public String get(Object key) {
        if (!(key instanceof String)) {
            throw new IllegalArgumentException(
                    "Type of key [" + key.getClass().getName() + "] must be java.lang.String");
        }
        return getSystemAttribute((String) key);
    }
