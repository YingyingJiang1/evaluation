    public String getMessage() {
        String result;
        try {
            // Important to use the default class loader, and not the one in
            // the GlobalProperties object. This is because the class loader in
            // the GlobalProperties is specified by the user for resolving
            // custom classes.
            final ResourceBundle resourceBundle = getBundle();
            final String pattern = resourceBundle.getString(key);
            final MessageFormat formatter = new MessageFormat(pattern, Locale.ROOT);
            result = formatter.format(args);
        }
        catch (final MissingResourceException ignored) {
            // If the Check author didn't provide i18n resource bundles
            // and logs audit event messages directly, this will return
            // the author's original message
            final MessageFormat formatter = new MessageFormat(key, Locale.ROOT);
            result = formatter.format(args);
        }
        return result;
    }
