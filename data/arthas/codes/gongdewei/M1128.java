    private ApiResponse processSessionInfoRequest(ApiRequest apiRequest, Session session) {
        ApiResponse response = new ApiResponse();
        Map<String, Object> body = new TreeMap<String, Object>();
        body.put("pid", session.getPid());
        body.put("createTime", session.getCreateTime());
        body.put("lastAccessTime", session.getLastAccessTime());

        response.setState(ApiState.SUCCEEDED)
                .setSessionId(session.getSessionId())
                //.setConsumerId(consumerId)
                .setBody(body);
        return response;
    }
