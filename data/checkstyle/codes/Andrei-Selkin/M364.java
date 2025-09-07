    private static boolean isValidLanguageCode(final String userSpecifiedLanguageCode) {
        boolean valid = false;
        final Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            if (userSpecifiedLanguageCode.equals(locale.toString())) {
                valid = true;
                break;
            }
        }
        return valid;
    }
