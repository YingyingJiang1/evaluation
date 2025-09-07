    public static void arthasUsageSuccess(String cmd, List<String> args) {
        if (statUrl == null) {
            return;
        }
        StringBuilder commandString = new StringBuilder(cmd);
        for (String arg : args) {
            commandString.append(" ").append(arg);
        }
        UserStatUtil.arthasUsage(cmd, commandString.toString());
    }
