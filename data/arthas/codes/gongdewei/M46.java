    private static void checkTelnetPortPid(Bootstrap bootstrap, long telnetPortPid, long targetPid) {
        if (telnetPortPid > 0 && targetPid != telnetPortPid) {
            AnsiLog.error("The telnet port {} is used by process {} instead of target process {}, you will connect to an unexpected process.",
                    bootstrap.getTelnetPortOrDefault(), telnetPortPid, targetPid);
            AnsiLog.error("1. Try to restart arthas-boot, select process {}, shutdown it first with running the 'stop' command.",
                            telnetPortPid);
            AnsiLog.error("2. Or try to stop the existing arthas instance: java -jar arthas-client.jar 127.0.0.1 {} -c \"stop\"", bootstrap.getTelnetPortOrDefault());
            AnsiLog.error("3. Or try to use different telnet port, for example: java -jar arthas-boot.jar --telnet-port 9998 --http-port -1");
            System.exit(1);
        }
    }
