    public static String cyan(String msg) {
        if (enableColor) {
            return colorStr(msg, CYAN);
        } else {
            return msg;
        }
    }
