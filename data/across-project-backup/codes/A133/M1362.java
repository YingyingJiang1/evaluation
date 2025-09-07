    static String latestVersion() {
        final String[] version = { "" };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URLConnection urlConnection = openURLConnection(ARTHAS_LATEST_VERSIONS_URL);
                    InputStream inputStream = urlConnection.getInputStream();
                    version[0] = com.taobao.arthas.common.IOUtils.toString(inputStream).trim();
                } catch (Throwable e) {
                    logger.debug("get latest version error", e);
                }
            }
        });

        thread.setDaemon(true);
        thread.start();
        try {
            thread.join(2000); // Wait up to 2 seconds for the version check
        } catch (Throwable e) {
            // Ignore
        }

        return version[0];
    }
