    public static String red(String msg) {
        if (enableColor) {
            return colorStr(msg, RED);
        } else {
            return msg;
        }
    }
