    private static Set<Field> getCheckMessageKeys(Class<?> module)
            throws MacroExecutionException {
        try {
            final Set<Field> checkstyleMessages = new HashSet<>();

            // get all fields from current class
            final Field[] fields = module.getDeclaredFields();

            for (Field field : fields) {
                if (field.getName().startsWith("MSG_")) {
                    checkstyleMessages.add(field);
                }
            }

            // deep scan class through hierarchy
            final Class<?> superModule = module.getSuperclass();

            if (superModule != null) {
                checkstyleMessages.addAll(getCheckMessageKeys(superModule));
            }

            // special cases that require additional classes
            if (module == RegexpMultilineCheck.class) {
                checkstyleMessages.addAll(getCheckMessageKeys(Class
                    .forName("com.puppycrawl.tools.checkstyle.checks.regexp.MultilineDetector")));
            }
            else if (module == RegexpSinglelineCheck.class
                    || module == RegexpSinglelineJavaCheck.class) {
                checkstyleMessages.addAll(getCheckMessageKeys(Class
                    .forName("com.puppycrawl.tools.checkstyle.checks.regexp.SinglelineDetector")));
            }

            return checkstyleMessages;
        }
        catch (ClassNotFoundException exc) {
            final String message = String.format(Locale.ROOT, "Couldn't find class: %s",
                    module.getName());
            throw new MacroExecutionException(message, exc);
        }
    }
