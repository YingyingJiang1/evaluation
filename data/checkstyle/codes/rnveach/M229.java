    public static boolean isCheckstyleModule(Class<?> clazz) {
        return AbstractAutomaticBean.class.isAssignableFrom(clazz)
                && !Modifier.isAbstract(clazz.getModifiers())
                && hasDefaultConstructor(clazz)
                && isNotXpathFileGenerator(clazz);
    }
