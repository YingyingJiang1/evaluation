    @Override
    public final void contextualize(Context context)
            throws CheckstyleException {
        final Collection<String> attributes = context.getAttributeNames();

        for (final String key : attributes) {
            final Object value = context.get(key);

            tryCopyProperty(key, value, false);
        }
    }
