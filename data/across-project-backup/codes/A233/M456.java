    private void setupLoadHandler() {
        final long initStartTime = System.currentTimeMillis();
        log.info("Setting up load handler at: {}", initStartTime);

        client.addLoadHandler(
                new CefLoadHandlerAdapter() {
                    @Override
                    public void onLoadingStateChange(
                            CefBrowser browser,
                            boolean isLoading,
                            boolean canGoBack,
                            boolean canGoForward) {
                        log.debug(
                                "Loading state change - isLoading: {}, canGoBack: {}, canGoForward:"
                                        + " {}, browserInitialized: {}, Time elapsed: {}ms",
                                isLoading,
                                canGoBack,
                                canGoForward,
                                browserInitialized,
                                System.currentTimeMillis() - initStartTime);

                        if (!isLoading && !browserInitialized) {
                            log.info(
                                    "Browser finished loading, preparing to initialize UI"
                                            + " components");
                            browserInitialized = true;
                            SwingUtilities.invokeLater(
                                    () -> {
                                        try {
                                            if (loadingWindow != null) {
                                                log.info("Starting UI initialization sequence");

                                                // Close loading window first
                                                loadingWindow.setVisible(false);
                                                loadingWindow.dispose();
                                                loadingWindow = null;
                                                log.info("Loading window disposed");

                                                // Then setup the main frame
                                                frame.setVisible(false);
                                                frame.dispose();
                                                frame.setOpacity(1.0f);
                                                frame.setUndecorated(false);
                                                frame.pack();
                                                frame.setSize(
                                                        UIScaling.scaleWidth(1280),
                                                        UIScaling.scaleHeight(800));
                                                frame.setLocationRelativeTo(null);
                                                log.debug("Frame reconfigured");

                                                // Show the main frame
                                                frame.setVisible(true);
                                                frame.requestFocus();
                                                frame.toFront();
                                                log.info("Main frame displayed and focused");

                                                // Focus the browser component
                                                Timer focusTimer =
                                                        new Timer(
                                                                100,
                                                                e -> {
                                                                    try {
                                                                        browser.getUIComponent()
                                                                                .requestFocus();
                                                                        log.info(
                                                                                "Browser component"
                                                                                        + " focused");
                                                                    } catch (Exception ex) {
                                                                        log.error(
                                                                                "Error focusing"
                                                                                        + " browser",
                                                                                ex);
                                                                    }
                                                                });
                                                focusTimer.setRepeats(false);
                                                focusTimer.start();
                                            }
                                        } catch (Exception e) {
                                            log.error("Error during UI initialization", e);
                                            // Attempt cleanup on error
                                            if (loadingWindow != null) {
                                                loadingWindow.dispose();
                                                loadingWindow = null;
                                            }
                                            if (frame != null) {
                                                frame.setVisible(true);
                                                frame.requestFocus();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
