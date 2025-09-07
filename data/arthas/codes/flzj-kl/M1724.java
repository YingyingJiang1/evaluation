    public static boolean monitorTargetPid (Integer pid)  {
        long tcpListenProcess = SocketUtils.findTcpListenProcess(NativeAgentConstants.ARTHAS_SERVER_HTTP_PORT);

        if (tcpListenProcess == -1) {
            try {
                JvmAttachmentHandler.attachJvmByPid(pid);
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if (tcpListenProcess == pid) {
            return true;
        }

        if (tcpListenProcess != pid) {
            String errorMsg = "Target port: " + NativeAgentConstants.ARTHAS_SERVER_HTTP_PORT
                    + " has been occupied by pid: " + tcpListenProcess;
            logger.error(errorMsg);
            return false;
        }

        return false;
    }
