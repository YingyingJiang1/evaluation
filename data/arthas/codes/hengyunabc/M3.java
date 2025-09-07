    public static List<String> readRemoteVersions() {
        InputStream inputStream = null;
        try {
            URLConnection connection = openURLConnection(ARTHAS_VERSIONS_URL);
            inputStream = connection.getInputStream();
            String versionsStr = IOUtils.toString(inputStream);
            String[] versions = versionsStr.split("\r\n");

            ArrayList<String> result = new ArrayList<String>();
            for (String version : versions) {
                result.add(version.trim());
            }
            return result;

        } catch (Throwable t) {
            AnsiLog.error("Can not read arthas versions from: " + ARTHAS_VERSIONS_URL);
            AnsiLog.debug(t);
        } finally {
            IOUtils.close(inputStream);
        }
        return null;
    }
