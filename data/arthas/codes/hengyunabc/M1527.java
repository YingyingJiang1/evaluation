    @Override
    public String dumpTraces(int maxTraces) {
        try {
            return execute0(maxTraces == 0 ? "traces" : "traces=" + maxTraces);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
