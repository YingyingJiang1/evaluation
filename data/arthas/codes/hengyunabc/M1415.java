    public static String cause(Throwable t) {
        if (null != t.getCause()) {
            return cause(t.getCause());
        }
        return t.getMessage();
    }
