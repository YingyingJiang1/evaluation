    public static String[] getClassloader(Class clazz) {
        List<String> list = new ArrayList<String>();
        ClassLoader loader = clazz.getClassLoader();
        if (null != loader) {
            list.add(loader.toString());
            while (true) {
                loader = loader.getParent();
                if (null == loader) {
                    break;
                }
                list.add(loader.toString());
            }
        }
        return list.toArray(new String[0]);
    }
