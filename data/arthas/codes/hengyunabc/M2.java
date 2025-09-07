    public static String readLatestReleaseVersion() {
        InputStream inputStream = null;
        try {
            URLConnection connection = openURLConnection(ARTHAS_LATEST_VERSIONS_URL);
            inputStream = connection.getInputStream();
            return IOUtils.toString(inputStream).trim();
        } catch (Throwable t) {
            AnsiLog.error("Can not read arthas version from: " + ARTHAS_LATEST_VERSIONS_URL);
            AnsiLog.debug(t);
        } finally {
            IOUtils.close(inputStream);
        }
        return null;
    }
