    private FullHttpResponse doListProcess(ChannelHandlerContext ctx, FullHttpRequest request) {
        Map<Long, String> processMap = null;
        try {
            processMap = ListJvmProcessHandler.listJvmProcessByInvoke();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<JavaProcessInfoDTO> javaProcessInfoList = new ArrayList<>();
        if (processMap != null) {
            processMap.forEach((pid, applicationName) -> {
                if (!"".equals(applicationName.replace(pid + " ", ""))) {
                    javaProcessInfoList.add(new JavaProcessInfoDTO(applicationName.replace(pid + " ", ""), pid.intValue()));
                }
            });
        }

        String processJson = JSON.toJSONString(javaProcessInfoList);

        DefaultFullHttpResponse response = buildHttpCorsResponse(processJson);
        return response;
    }
