    public static String getCodeSource(final CodeSource cs) {
        if (null == cs || null == cs.getLocation() || null == cs.getLocation().getFile()) {
            return com.taobao.arthas.core.util.Constants.EMPTY_STRING;
        }

        return cs.getLocation().getFile();
    }
