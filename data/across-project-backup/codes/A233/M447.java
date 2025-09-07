    private void configureCefSettings(CefAppBuilder builder) {
        CefSettings settings = builder.getCefSettings();
        String basePath = InstallationPathConfig.getClientWebUIPath();
        log.info("basePath " + basePath);
        settings.cache_path = new File(basePath + "cache").getAbsolutePath();
        settings.root_cache_path = new File(basePath + "root_cache").getAbsolutePath();
        //        settings.browser_subprocess_path = new File(basePath +
        // "subprocess").getAbsolutePath();
        //        settings.resources_dir_path = new File(basePath + "resources").getAbsolutePath();
        //        settings.locales_dir_path = new File(basePath + "locales").getAbsolutePath();
        settings.log_file = new File(basePath, "debug.log").getAbsolutePath();

        settings.persist_session_cookies = true;
        settings.windowless_rendering_enabled = false;
        settings.log_severity = CefSettings.LogSeverity.LOGSEVERITY_INFO;

        builder.setAppHandler(
                new MavenCefAppHandlerAdapter() {
                    @Override
                    public void stateHasChanged(org.cef.CefApp.CefAppState state) {
                        log.info("CEF state changed: " + state);
                        if (state == CefApp.CefAppState.TERMINATED) {
                            System.exit(0);
                        }
                    }
                });
    }
