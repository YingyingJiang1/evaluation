    @Override
    public Object getProperty(String name) {
        String actualName = resolvePropertyName(name);
        return super.getProperty(actualName);
    }
