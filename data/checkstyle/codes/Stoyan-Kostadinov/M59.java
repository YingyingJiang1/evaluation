    public static Object getFieldValue(Field field, Object instance)
            throws MacroExecutionException {
        try {
            // required for package/private classes
            field.trySetAccessible();
            return field.get(instance);
        }
        catch (IllegalAccessException exc) {
            throw new MacroExecutionException("Couldn't get field value", exc);
        }
    }
