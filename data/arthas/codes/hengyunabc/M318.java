    public static String green(String msg) {
        if (enableColor) {
            return colorStr(msg, GREEN);
        } else {
            return msg;
        }
    }
