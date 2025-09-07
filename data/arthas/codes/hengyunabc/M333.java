    public static void warn(String msg) {
        if (canLog(Level.WARNING)) {
            if (enableColor) {
                System.out.println(WARN_COLOR_PREFIX + msg);
            } else {
                System.out.println(WARN_PREFIX + msg);
            }
        }
    }
