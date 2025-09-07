    public static String findClientIP(HttpHeaders headers) {
        String hostStr = headers.get("X-Forwarded-For");
        if (hostStr == null) {
            return null;
        }
        int index = hostStr.indexOf(',');
        if (index > 0) {
            hostStr = hostStr.substring(0, index);
        }
        return hostStr;
    }
