    private ApiResponse processCloseSessionRequest(ApiRequest apiRequest, Session session) {
        sessionManager.removeSession(session.getSessionId());
        ApiResponse response = new ApiResponse();
        response.setState(ApiState.SUCCEEDED);
        return response;
    }
