    public static String getThreadTitle(StackModel stackModel) {
        StringBuilder sb = new StringBuilder("thread_name=");
        sb.append(stackModel.getThreadName())
                .append(";id=").append(stackModel.getThreadId())
                .append(";is_daemon=").append(stackModel.isDaemon())
                .append(";priority=").append(stackModel.getPriority())
                .append(";TCCL=").append(stackModel.getClassloader());
        if (stackModel.getTraceId() != null) {
            sb.append(";trace_id=").append(stackModel.getTraceId());
        }
        if (stackModel.getRpcId() != null) {
            sb.append(";rpc_id=").append(stackModel.getRpcId());
        }
        return sb.toString();
    }
