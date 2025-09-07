    @Override
    public String name() {
        if (shouldOverridesName(clazz)) {
            try {
                return clazz.newInstance().name();
            } catch (Exception ignore) {
                // Use cli.getName() instead
            }
        }
        return cli.getName();
    }
