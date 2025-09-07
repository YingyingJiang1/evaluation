    public static Constructor findConstructor(String desc, ClassLoader loader) {
        try {
            int lparen = desc.indexOf('(');
            String className = desc.substring(0, lparen).trim();
            return getClass(className, loader).getConstructor(parseTypes(desc, loader));
        } catch (ClassNotFoundException ex) {
            throw new ReflectException(ex);
        } catch (NoSuchMethodException ex) {
            throw new ReflectException(ex);
        }
    }
