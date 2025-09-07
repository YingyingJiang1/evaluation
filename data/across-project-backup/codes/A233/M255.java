    @Around("@annotation(autoJobPostMapping)")
    public Object wrapWithJobExecution(
            ProceedingJoinPoint joinPoint, AutoJobPostMapping autoJobPostMapping) {
        // This aspect will run before any audit aspects due to @Order(0)
        // Extract parameters from the request and annotation
        boolean async = Boolean.parseBoolean(request.getParameter("async"));
        log.debug(
                "AutoJobAspect: Processing {} {} with async={}",
                request.getMethod(),
                request.getRequestURI(),
                async);
        long timeout = autoJobPostMapping.timeout();
        int retryCount = autoJobPostMapping.retryCount();
        boolean trackProgress = autoJobPostMapping.trackProgress();

        log.debug(
                "AutoJobPostMapping execution with async={}, timeout={}, retryCount={}, trackProgress={}",
                async,
                timeout > 0 ? timeout : "default",
                retryCount,
                trackProgress);

        // Process arguments in-place to avoid type mismatch issues
        Object[] args = processArgsInPlace(joinPoint.getArgs(), async);

        // Extract queueable and resourceWeight parameters and validate
        boolean queueable = autoJobPostMapping.queueable();
        int resourceWeight = Math.max(1, Math.min(100, autoJobPostMapping.resourceWeight()));

        // Integrate with the JobExecutorService
        if (retryCount <= 1) {
            // No retries needed, simple execution
            return jobExecutorService.runJobGeneric(
                    async,
                    () -> {
                        try {
                            // Note: Progress tracking is handled in TaskManager/JobExecutorService
                            // The trackProgress flag controls whether detailed progress is stored
                            // for REST API queries, not WebSocket notifications
                            return joinPoint.proceed(args);
                        } catch (Throwable ex) {
                            log.error(
                                    "AutoJobAspect caught exception during job execution: {}",
                                    ex.getMessage(),
                                    ex);
                            throw new RuntimeException(ex);
                        }
                    },
                    timeout,
                    queueable,
                    resourceWeight);
        } else {
            // Use retry logic
            return executeWithRetries(
                    joinPoint,
                    args,
                    async,
                    timeout,
                    retryCount,
                    trackProgress,
                    queueable,
                    resourceWeight);
        }
    }
