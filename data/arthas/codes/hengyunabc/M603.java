    private static String execute(AsyncProfiler asyncProfiler, String arg)
            throws IllegalArgumentException, IOException {
        logger.info("profiler execute args: {}", arg);
        String result = asyncProfiler.execute(arg);
        if (!result.endsWith("\n")) {
            result += "\n";
        }
        return result;
    }
