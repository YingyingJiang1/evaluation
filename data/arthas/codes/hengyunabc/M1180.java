    @Override
    public Term stdoutHandler(io.termd.core.function.Function<String, String>  handler) {
        if (stdoutHandlerChain == null) {
            stdoutHandlerChain = new ArrayList<io.termd.core.function.Function<String, String>>();
        }
        stdoutHandlerChain.add(handler);
        return this;
    }
