    @Override
    public void invokeThrowTracing(ClassLoader classLoader, String tracingClassName, String tracingMethodName, String tracingMethodDesc, int tracingLineNumber)
            throws Throwable {
        threadLocalTraceEntity(classLoader).tree.end(true);
    }
