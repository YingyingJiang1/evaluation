    public static ClassVO createSimpleClassInfo(Class clazz) {
        ClassVO classInfo = new ClassVO();
        fillSimpleClassVO(clazz, classInfo);
        return classInfo;
    }
