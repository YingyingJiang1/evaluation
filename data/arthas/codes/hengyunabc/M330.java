    public static void info(String msg) {
        if (canLog(Level.CONFIG)) {
            if (enableColor) {
                System.out.println(INFO_COLOR_PREFIX + msg);
            } else {
                System.out.println(INFO_PREFIX + msg);
            }
        }
    }
