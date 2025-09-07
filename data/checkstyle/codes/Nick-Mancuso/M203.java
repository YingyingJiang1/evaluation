    public static URI getUriByFilename(String filename) throws CheckstyleException {
        URI uri = getWebOrFileProtocolUri(filename);

        if (uri == null) {
            uri = getFilepathOrClasspathUri(filename);
        }

        return uri;
    }
