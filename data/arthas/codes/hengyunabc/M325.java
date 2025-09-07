    public static void trace(String format, Object... arguments) {
        if (canLog(Level.FINEST)) {
            trace(format(format, arguments));
        }
    }
