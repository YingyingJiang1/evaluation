    private static long findTcpListenProcessOnUnix(int port) {
        String pid = ExecutingCommand.getFirstAnswer("lsof -t -s TCP:LISTEN -i TCP:" + port);
        if (pid != null && !pid.trim().isEmpty()) {
            try {
                return Long.parseLong(pid.trim());
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        return -1;
    }
