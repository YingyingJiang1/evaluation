    private static long findTcpListenProcessOnWindows(int port) {
        String[] command = { "netstat", "-ano", "-p", "TCP" };
        List<String> lines = ExecutingCommand.runNative(command);
        for (String line : lines) {
            if (line.contains("LISTENING")) {
                // TCP 0.0.0.0:49168 0.0.0.0:0 LISTENING 476
                String[] strings = line.trim().split("\\s+");
                if (strings.length == 5) {
                    if (strings[1].endsWith(":" + port)) {
                        return Long.parseLong(strings[4]);
                    }
                }
            }
        }
        return -1;
    }
