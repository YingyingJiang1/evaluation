    private static URI getFilepathOrClasspathUri(String filename) throws CheckstyleException {
        final URI uri;
        final File file = new File(filename);

        if (file.exists()) {
            uri = file.toURI();
        }
        else {
            final int lastIndexOfClasspathProtocol;
            if (filename.lastIndexOf(CLASSPATH_URL_PROTOCOL) == 0) {
                lastIndexOfClasspathProtocol = CLASSPATH_URL_PROTOCOL.length();
            }
            else {
                lastIndexOfClasspathProtocol = 0;
            }
            uri = getResourceFromClassPath(filename
                .substring(lastIndexOfClasspathProtocol));
        }
        return uri;
    }
