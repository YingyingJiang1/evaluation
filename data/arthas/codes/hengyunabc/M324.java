    public static void trace(String msg) {
        if (canLog(Level.FINEST)) {
            if (enableColor) {
                System.out.println(TRACE_COLOR_PREFIX + msg);
            } else {
                System.out.println(TRACE_PREFIX + msg);
            }
        }
    }
