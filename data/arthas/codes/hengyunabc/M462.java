    @Override
    public Object setPossibleProperty(Map context, Object target, String name, Object value) throws OgnlException {
        if (GlobalOptions.strict) {
            throw new IllegalAccessError(GlobalOptions.STRICT_MESSAGE);
        }
        return super.setPossibleProperty(context, target, name, value);
    }
