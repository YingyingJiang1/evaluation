    private static boolean isVersionAtLeast(String actualVersion,
                                            String requiredVersion) {
        final Version actualVersionParsed = Version.parse(actualVersion);
        final Version requiredVersionParsed = Version.parse(requiredVersion);

        return actualVersionParsed.compareTo(requiredVersionParsed) >= 0;
    }
