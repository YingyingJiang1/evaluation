    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        String appLocaleEnv = applicationProperties.getSystem().getDefaultLocale();
        Locale defaultLocale = // Fallback to UK locale if environment variable is not set
                Locale.UK;
        if (appLocaleEnv != null && !appLocaleEnv.isEmpty()) {
            Locale tempLocale = Locale.forLanguageTag(appLocaleEnv);
            String tempLanguageTag = tempLocale.toLanguageTag();
            if (appLocaleEnv.equalsIgnoreCase(tempLanguageTag)) {
                defaultLocale = tempLocale;
            } else {
                tempLocale = Locale.forLanguageTag(appLocaleEnv.replace("_", "-"));
                tempLanguageTag = tempLocale.toLanguageTag();
                if (appLocaleEnv.equalsIgnoreCase(tempLanguageTag)) {
                    defaultLocale = tempLocale;
                } else {
                    System.err.println(
                            "Invalid SYSTEM_DEFAULTLOCALE environment variable value. Falling back to default en-GB.");
                }
            }
        }
        slr.setDefaultLocale(defaultLocale);
        return slr;
    }
