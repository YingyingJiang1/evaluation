            @Override
            public void call() {
                circuitBreaker.markNonSuccess();
                if (_cmd.commandState.compareAndSet(CommandState.OBSERVABLE_CHAIN_CREATED, CommandState.UNSUBSCRIBED)) {
                    if (!_cmd.executionResult.containsTerminalEvent()) {
                        _cmd.eventNotifier.markEvent(HystrixEventType.CANCELLED, _cmd.commandKey);
                        try {
                            executionHook.onUnsubscribe(_cmd);
                        } catch (Throwable hookEx) {
                            logger.warn("Error calling HystrixCommandExecutionHook.onUnsubscribe", hookEx);
                        }
                        _cmd.executionResultAtTimeOfCancellation = _cmd.executionResult
                                .addEvent((int) (System.currentTimeMillis() - _cmd.commandStartTimestamp), HystrixEventType.CANCELLED);
                    }
                    handleCommandEnd(false); //user code never ran
                } else if (_cmd.commandState.compareAndSet(CommandState.USER_CODE_EXECUTED, CommandState.UNSUBSCRIBED)) {
                    if (!_cmd.executionResult.containsTerminalEvent()) {
                        _cmd.eventNotifier.markEvent(HystrixEventType.CANCELLED, _cmd.commandKey);
                        try {
                            executionHook.onUnsubscribe(_cmd);
                        } catch (Throwable hookEx) {
                            logger.warn("Error calling HystrixCommandExecutionHook.onUnsubscribe", hookEx);
                        }
                        _cmd.executionResultAtTimeOfCancellation = _cmd.executionResult
                                .addEvent((int) (System.currentTimeMillis() - _cmd.commandStartTimestamp), HystrixEventType.CANCELLED);
                    }
                    handleCommandEnd(true); //user code did run
                }
            }
