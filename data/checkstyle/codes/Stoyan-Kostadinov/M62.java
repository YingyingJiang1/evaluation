    public static String getParentModule(Class<?> moduleClass)
                throws MacroExecutionException {
        String parentModuleName = "";
        Class<?> parentClass = moduleClass.getSuperclass();

        while (parentClass != null) {
            parentModuleName = CLASS_TO_PARENT_MODULE.get(parentClass);
            if (parentModuleName != null) {
                break;
            }
            parentClass = parentClass.getSuperclass();
        }

        // If parent class is not found, check interfaces
        if (parentModuleName == null || parentModuleName.isEmpty()) {
            final Class<?>[] interfaces = moduleClass.getInterfaces();
            for (Class<?> interfaceClass : interfaces) {
                parentModuleName = CLASS_TO_PARENT_MODULE.get(interfaceClass);
                if (parentModuleName != null) {
                    break;
                }
            }
        }

        if (parentModuleName == null || parentModuleName.isEmpty()) {
            final String message = String.format(Locale.ROOT,
                    "Failed to find parent module for %s", moduleClass.getSimpleName());
            throw new MacroExecutionException(message);
        }

        return parentModuleName;
    }
