    @Override
    public final void configure(Configuration config)
            throws CheckstyleException {
        configuration = config;

        final String[] attributes = config.getPropertyNames();

        for (final String key : attributes) {
            final String value = config.getProperty(key);

            tryCopyProperty(key, value, true);
        }

        finishLocalSetup();

        final Configuration[] childConfigs = config.getChildren();
        for (final Configuration childConfig : childConfigs) {
            setupChild(childConfig);
        }
    }
