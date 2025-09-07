    @Override
    public void execute(Sink sink, MacroRequest request) throws MacroExecutionException {
        // until https://github.com/checkstyle/checkstyle/issues/13426
        if (!(sink instanceof XdocSink)) {
            throw new MacroExecutionException("Expected Sink to be an XdocSink.");
        }
        final String checkName = (String) request.getParameter("checkName");
        final Object instance = SiteUtil.getModuleInstance(checkName);
        final Class<?> clss = instance.getClass();
        final Set<String> messageKeys = SiteUtil.getMessageKeys(clss);
        createListOfMessages((XdocSink) sink, clss, messageKeys);
    }
