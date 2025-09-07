    private static String extractBaseName(String fileName) {
        final String regexp;
        final Matcher languageCountryVariantMatcher =
            LANGUAGE_COUNTRY_VARIANT_PATTERN.matcher(fileName);
        final Matcher languageCountryMatcher = LANGUAGE_COUNTRY_PATTERN.matcher(fileName);
        final Matcher languageMatcher = LANGUAGE_PATTERN.matcher(fileName);
        if (languageCountryVariantMatcher.matches()) {
            regexp = LANGUAGE_COUNTRY_VARIANT_PATTERN.pattern();
        }
        else if (languageCountryMatcher.matches()) {
            regexp = LANGUAGE_COUNTRY_PATTERN.pattern();
        }
        else if (languageMatcher.matches()) {
            regexp = LANGUAGE_PATTERN.pattern();
        }
        else {
            regexp = DEFAULT_TRANSLATION_REGEXP;
        }
        // We use substring(...) instead of replace(...), so that the regular expression does
        // not have to be compiled each time it is used inside 'replace' method.
        final String removePattern = regexp.substring("^.+".length());
        return fileName.replaceAll(removePattern, "");
    }
