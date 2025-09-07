    @Override
    public boolean accept(String uri) {
        boolean result = true;
        for (BeforeExecutionFileFilter filter : beforeExecutionFileFilters) {
            if (!filter.accept(uri)) {
                result = false;
                break;
            }
        }
        return result;
    }
