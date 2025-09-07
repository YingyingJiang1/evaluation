    public static DemoBootstrap getRunningInstance() {
        if (demoBootstrap == null) {
            throw new IllegalStateException("AllServerStart must be initialized before!");
        }
        return demoBootstrap;
    }
