    public static InputSource sourceFromFilename(String filename) throws CheckstyleException {
        // figure out if this is a File or a URL
        final URI uri = getUriByFilename(filename);
        return new InputSource(uri.toASCIIString());
    }
