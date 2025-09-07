    public static void fillSimpleClassVO(Class clazz, ClassVO classInfo) {
        classInfo.setName(StringUtils.classname(clazz));
        classInfo.setClassloader(TypeRenderUtils.getClassloader(clazz));
        classInfo.setClassLoaderHash(StringUtils.classLoaderHash(clazz));
    }
