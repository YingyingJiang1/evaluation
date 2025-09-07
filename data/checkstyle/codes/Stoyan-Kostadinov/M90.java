    @Override
    public void execute(Sink sink, MacroRequest request) throws MacroExecutionException {
        // until https://github.com/checkstyle/checkstyle/issues/13426
        if (!(sink instanceof XdocSink)) {
            throw new MacroExecutionException("Expected Sink to be an XdocSink.");
        }

        final String modulePath = (String) request.getParameter("modulePath");

        configureGlobalProperties(modulePath);

        writePropertiesTable((XdocSink) sink);
    }
