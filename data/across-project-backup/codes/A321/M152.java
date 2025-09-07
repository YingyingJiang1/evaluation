    public static HystrixEventType from(HystrixRollingNumberEvent event) {
        switch (event) {
            case EMIT: return EMIT;
            case SUCCESS: return SUCCESS;
            case FAILURE: return FAILURE;
            case TIMEOUT: return TIMEOUT;
            case SHORT_CIRCUITED: return SHORT_CIRCUITED;
            case THREAD_POOL_REJECTED: return THREAD_POOL_REJECTED;
            case SEMAPHORE_REJECTED: return SEMAPHORE_REJECTED;
            case FALLBACK_EMIT: return FALLBACK_EMIT;
            case FALLBACK_SUCCESS: return FALLBACK_SUCCESS;
            case FALLBACK_FAILURE: return FALLBACK_FAILURE;
            case FALLBACK_REJECTION: return FALLBACK_REJECTION;
            case FALLBACK_DISABLED: return FALLBACK_DISABLED;
            case FALLBACK_MISSING: return FALLBACK_MISSING;
            case EXCEPTION_THROWN: return EXCEPTION_THROWN;
            case RESPONSE_FROM_CACHE: return RESPONSE_FROM_CACHE;
            case COLLAPSED: return COLLAPSED;
            case BAD_REQUEST: return BAD_REQUEST;
            case COMMAND_MAX_ACTIVE: return COMMAND_MAX_ACTIVE;
            default:
                throw new RuntimeException("Not an event that can be converted to HystrixEventType : " + event);
        }
    }
