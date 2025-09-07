    public static HystrixRollingNumberEvent from(HystrixEventType eventType) {
        switch (eventType) {
            case BAD_REQUEST: return HystrixRollingNumberEvent.BAD_REQUEST;
            case COLLAPSED: return HystrixRollingNumberEvent.COLLAPSED;
            case EMIT: return HystrixRollingNumberEvent.EMIT;
            case EXCEPTION_THROWN: return HystrixRollingNumberEvent.EXCEPTION_THROWN;
            case FAILURE: return HystrixRollingNumberEvent.FAILURE;
            case FALLBACK_EMIT: return HystrixRollingNumberEvent.FALLBACK_EMIT;
            case FALLBACK_FAILURE: return HystrixRollingNumberEvent.FALLBACK_FAILURE;
            case FALLBACK_DISABLED: return HystrixRollingNumberEvent.FALLBACK_DISABLED;
            case FALLBACK_MISSING: return HystrixRollingNumberEvent.FALLBACK_MISSING;
            case FALLBACK_REJECTION: return HystrixRollingNumberEvent.FALLBACK_REJECTION;
            case FALLBACK_SUCCESS: return HystrixRollingNumberEvent.FALLBACK_SUCCESS;
            case RESPONSE_FROM_CACHE: return HystrixRollingNumberEvent.RESPONSE_FROM_CACHE;
            case SEMAPHORE_REJECTED: return HystrixRollingNumberEvent.SEMAPHORE_REJECTED;
            case SHORT_CIRCUITED: return HystrixRollingNumberEvent.SHORT_CIRCUITED;
            case SUCCESS: return HystrixRollingNumberEvent.SUCCESS;
            case THREAD_POOL_REJECTED: return HystrixRollingNumberEvent.THREAD_POOL_REJECTED;
            case TIMEOUT: return HystrixRollingNumberEvent.TIMEOUT;
            default: throw new RuntimeException("Unknown HystrixEventType : " + eventType);
        }
    }
