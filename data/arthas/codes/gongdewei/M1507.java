    public static List<ClassLoaderVO> createClassLoaderVOList(Collection<ClassLoader> classLoaders) {
        List<ClassLoaderVO> classLoaderVOList = new ArrayList<ClassLoaderVO>();
        for (ClassLoader classLoader : classLoaders) {
            classLoaderVOList.add(createClassLoaderVO(classLoader));
        }
        return classLoaderVOList;
    }
