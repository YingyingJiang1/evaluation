    public synchronized static DemoBootstrap getInstance() throws Throwable {
        if (demoBootstrap == null) {
            demoBootstrap = new DemoBootstrap();
        }
        return demoBootstrap;
    }
