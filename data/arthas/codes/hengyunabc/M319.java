    public static String yellow(String msg) {
        if (enableColor) {
            return colorStr(msg, YELLOW);
        } else {
            return msg;
        }
    }
