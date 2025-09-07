    @Override
    public String cacheLocation() {
        if (processOutput != null) {
            return processOutput.cacheLocation;
        }
        return null;
    }
