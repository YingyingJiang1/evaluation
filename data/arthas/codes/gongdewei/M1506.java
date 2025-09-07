    public static ClassLoaderVO createClassLoaderVO(ClassLoader classLoader) {
        ClassLoaderVO classLoaderVO = new ClassLoaderVO();
        classLoaderVO.setHash(classLoaderHash(classLoader));
        classLoaderVO.setName(classLoader==null?"BootstrapClassLoader":classLoader.toString());
        ClassLoader parent = classLoader == null ? null : classLoader.getParent();
        classLoaderVO.setParent(parent==null?null:parent.toString());
        return classLoaderVO;
    }
