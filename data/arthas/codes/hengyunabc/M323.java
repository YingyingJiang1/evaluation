    public static String white(String msg) {
        if (enableColor) {
            return colorStr(msg, WHITE);
        } else {
            return msg;
        }
    }
