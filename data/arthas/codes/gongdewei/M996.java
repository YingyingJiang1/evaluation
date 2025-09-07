    public static ExitStatus failure(int statusCode, String message) {
        if (statusCode == 0) {
            throw new IllegalArgumentException("failure status code cannot be 0");
        }
        return new ExitStatus(statusCode, message);
    }
