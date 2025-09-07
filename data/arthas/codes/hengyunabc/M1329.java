    private void cleanUpSpyReference() {
        try {
            SpyAPI.setNopSpy();
            SpyAPI.destroy();
        } catch (Throwable e) {
            // ignore
        }
        // AgentBootstrap.resetArthasClassLoader();
        try {
            Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass("com.taobao.arthas.agent334.AgentBootstrap");
            Method method = clazz.getDeclaredMethod("resetArthasClassLoader");
            method.invoke(null);
        } catch (Throwable e) {
            // ignore
        }
    }
