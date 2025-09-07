    private static Charset createCharset(String name) {
        if (!Charset.isSupported(name)) {
            final String message = "unsupported charset: '" + name + "'";
            throw new UnsupportedCharsetException(message);
        }
        return Charset.forName(name);
    }
