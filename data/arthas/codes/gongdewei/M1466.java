    public static String[] getAnnotations(Annotation[] annotations) {
        List<String> list = new ArrayList<String>();
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                list.add(StringUtils.classname(annotation.annotationType()));
            }
        }
        return list.toArray(new String[0]);
    }
