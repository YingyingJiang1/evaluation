    public static Integer findClientPort(HttpHeaders headers) {
        String portStr = headers.get("X-Real-Port");
        if (portStr != null) {
            return Integer.parseInt(portStr);
        }
        return null;
    }
