    public static Map<Long, String> listJvmProcessByInvoke()  {
        if (ARTHAS_HOME_DIR == null) {
            ArthasHomeHandler.findArthasHome();
        }

        if (ARTHAS_HOME_DIR == null) {
            return null;
        }

        String arthasBootPath = ARTHAS_HOME_DIR + File.separator + "arthas-boot.jar";
        Method method = null;
        Object instance = null;
        Map<Long, String> result = null;

        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{new File(arthasBootPath).toURI().toURL()});

            Class<?> clazz = classLoader.loadClass(PROCESS_UTILS_PATH);

            method = clazz.getDeclaredMethod(LIST_PROCESS_BY_JPS_METHOD, boolean.class);
            method.setAccessible(true);

            instance = clazz.getDeclaredConstructor().newInstance();

            result = (Map<Long, String>) method.invoke(instance, false);
        } catch (Exception e) {
            logger.error("invoke list java  process failed:" + e.getMessage());
            throw new RuntimeException(e);
        }

        return result;
    }
