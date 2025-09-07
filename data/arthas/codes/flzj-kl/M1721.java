    private static void verifyArthasHome(String arthasHome) {
        File home = new File(arthasHome);
        if (home.isDirectory()) {
            String[] fileList = {"arthas-core.jar", "arthas-agent.jar", "arthas-spy.jar"};

            for (String fileName : fileList) {
                if (!new File(home, fileName).exists()) {
                    logger.error("Please ensure that arthas-native agent-client is in the same directory as arthas-core.jar, arthas-agent.jar, and arthas-spy.jar");
                    throw new IllegalArgumentException(
                            fileName + " do not exist, arthas home: " + home.getAbsolutePath());
                }
            }
            return;
        }

        throw new IllegalArgumentException("illegal arthas home: " + home.getAbsolutePath());
    }
