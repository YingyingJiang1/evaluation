    @Override
    public Object apply(Object object, String name, Object value) {
        if (value instanceof ObjectVO) {
            ObjectVO vo = (ObjectVO) value;
            String resultStr = StringUtils.objectToString(vo.needExpand() ? new ObjectView(vo).draw() : value);
            return resultStr;
        }
        return value;
    }
