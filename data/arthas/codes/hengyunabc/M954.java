    public static ObjectVO[] array(Object[] objects, Integer expand) {
        ObjectVO[] result = new ObjectVO[objects.length];
        for (int i = 0; i < objects.length; ++i) {
            result[i] = new ObjectVO(objects[i], expand);
        }
        return result;
    }
