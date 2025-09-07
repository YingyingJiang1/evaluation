    private String getCodeSource(final CodeSource cs) {
        if (null == cs
                || null == cs.getLocation()
                || null == cs.getLocation().getFile()) {
            return Constants.EMPTY_STRING;
        }

        return cs.getLocation().getFile();
    }
