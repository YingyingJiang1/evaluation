    private ApiResponse dispatchRequest(ApiAction action, ApiRequest apiRequest, Session session) throws ApiException {
        switch (action) {
            case EXEC:
                return processExecRequest(apiRequest, session);
            case ASYNC_EXEC:
                return processAsyncExecRequest(apiRequest, session);
            case INTERRUPT_JOB:
                return processInterruptJob(apiRequest, session);
            case PULL_RESULTS:
                return processPullResultsRequest(apiRequest, session);
            case SESSION_INFO:
                return processSessionInfoRequest(apiRequest, session);
            case JOIN_SESSION:
                return processJoinSessionRequest(apiRequest, session);
            case CLOSE_SESSION:
                return processCloseSessionRequest(apiRequest, session);
            case INIT_SESSION:
                break;
        }
        return null;
    }
