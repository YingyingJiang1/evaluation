    private static File findToolsJar(String javaHome) {
        if (JavaVersionUtils.isGreaterThanJava8()) {
            return null;
        }

        File toolsJar = new File(javaHome, "lib/tools.jar");
        if (!toolsJar.exists()) {
            toolsJar = new File(javaHome, "../lib/tools.jar");
        }
        if (!toolsJar.exists()) {
            // maybe jre
            toolsJar = new File(javaHome, "../../lib/tools.jar");
        }

        if (!toolsJar.exists()) {
            throw new IllegalArgumentException("Can not find tools.jar under java home: " + javaHome);
        }

        AnsiLog.debug("Found tools.jar: " + toolsJar.getAbsolutePath());
        return toolsJar;
    }
