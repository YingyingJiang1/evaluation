    public static void error(String msg) {
        if (canLog(Level.SEVERE)) {
            if (enableColor) {
                System.out.println(ERROR_COLOR_PREFIX + msg);
            } else {
                System.out.println(ERROR_PREFIX + msg);
            }
        }
    }
