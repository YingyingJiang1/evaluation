    public static String blue(String msg) {
        if (enableColor) {
            return colorStr(msg, BLUE);
        } else {
            return msg;
        }
    }
