    public static Method findMethod(String desc, ClassLoader loader) {
        try {
            int lparen = desc.indexOf('(');
            int dot = desc.lastIndexOf('.', lparen);
            String className = desc.substring(0, dot).trim();
            String methodName = desc.substring(dot + 1, lparen).trim();
            return getClass(className, loader).getDeclaredMethod(methodName, parseTypes(desc, loader));
        } catch (ClassNotFoundException ex) {
            throw new ReflectException(ex);
        } catch (NoSuchMethodException ex) {
            throw new ReflectException(ex);
        }
    }
