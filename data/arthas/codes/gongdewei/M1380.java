    public static void end(CommandProcess process, ExitStatus status) {
        if (status != null) {
            process.end(status.getStatusCode(), status.getMessage());
        } else {
            process.end(-1, "process error, exit status is null");
        }
    }
