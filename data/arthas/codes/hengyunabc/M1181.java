    @Override
    public Term write(String data) {
        if (stdoutHandlerChain != null) {
            for (io.termd.core.function.Function<String, String> function : stdoutHandlerChain) {
                data = function.apply(data);
            }
        }
        conn.write(data);
        return this;
    }
