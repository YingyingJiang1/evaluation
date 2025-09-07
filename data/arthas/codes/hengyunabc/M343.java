    private static long doFindTcpListenProcess(int port) {
        try {
            if (OSUtils.isWindows()) {
                return findTcpListenProcessOnWindows(port);
            }

            if (OSUtils.isLinux() || OSUtils.isMac()) {
                return findTcpListenProcessOnUnix(port);
            }
        } catch (Throwable e) {
            // ignore
        }
        return -1;
    }
