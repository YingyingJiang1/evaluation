    private static String[] getActiveProfile(String[] args) {
        // 1. Check for explicitly passed profiles
        if (args != null) {
            for (String arg : args) {
                if (arg.startsWith("--spring.profiles.active=")) {
                    String[] provided = arg.substring(arg.indexOf('=') + 1).split(",");
                    if (provided.length > 0) {
                        return provided;
                    }
                }
            }
        }

        // 2. Detect if SecurityConfiguration is present on classpath
        if (isClassPresent(
                "stirling.software.proprietary.security.configuration.SecurityConfiguration")) {
            log.info("Additional features in jar");
            return new String[] {"security"};
        } else {
            log.info("Without additional features in jar");
            return new String[] {"default"};
        }
    }
