    @Override
    public boolean matching(String target) {
        return null != target
                && null != pattern
                && target.matches(pattern);
    }
