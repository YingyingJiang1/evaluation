    public static String black(String msg) {
        if (enableColor) {
            return colorStr(msg, BLACK);
        } else {
            return msg;
        }
    }
