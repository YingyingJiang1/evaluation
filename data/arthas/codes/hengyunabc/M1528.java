    @Override
    public String dumpFlat(int maxMethods) {
        try {
            return execute0(maxMethods == 0 ? "flat" : "flat=" + maxMethods);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
