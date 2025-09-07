    public static Set<String> getMessageKeys(Class<?> module)
            throws MacroExecutionException {
        final Set<Field> messageKeyFields = getCheckMessageKeys(module);
        // We use a TreeSet to sort the message keys alphabetically
        final Set<String> messageKeys = new TreeSet<>();
        for (Field field : messageKeyFields) {
            messageKeys.add(getFieldValue(field, module).toString());
        }
        return messageKeys;
    }
