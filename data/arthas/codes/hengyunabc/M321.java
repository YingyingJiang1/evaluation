    public static String magenta(String msg) {
        if (enableColor) {
            return colorStr(msg, MAGENTA);
        } else {
            return msg;
        }
    }
