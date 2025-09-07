    public static String[] getClassNameList(Class[] classes) {
        List<String> list = new ArrayList<String>();
        for (Class anInterface : classes) {
            list.add(StringUtils.classname(anInterface));
        }
        return list.toArray(new String[0]);
    }
