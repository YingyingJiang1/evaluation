    public static boolean isAllZeroIP(String ipStr) {
        if (ipStr == null || ipStr.isEmpty()) {
            return false;
        }
        char[] charArray = ipStr.toCharArray();

        for (char c : charArray) {
            if (c != '0' && c != '.' && c != ':') {
                return false;
            }
        }

        return true;
    }
