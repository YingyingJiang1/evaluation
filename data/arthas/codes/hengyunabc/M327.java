    public static void debug(String msg) {
        if (canLog(Level.FINER)) {
            if (enableColor) {
                System.out.println(DEBUG_COLOR_PREFIX + msg);
            } else {
                System.out.println(DEBUG_PREFIX + msg);
            }
        }
    }
