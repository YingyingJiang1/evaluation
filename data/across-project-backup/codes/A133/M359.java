    public static Object newInstance(final Constructor cstruct, final Object[] args) {
        boolean flag = cstruct.isAccessible();
        try {
            if (!flag) {
                cstruct.setAccessible(true);
            }
            Object result = cstruct.newInstance(args);
            return result;
        } catch (InstantiationException e) {
            throw new ReflectException(e);
        } catch (IllegalAccessException e) {
            throw new ReflectException(e);
        } catch (InvocationTargetException e) {
            throw new ReflectException(e.getTargetException());
        } finally {
            if (!flag) {
                cstruct.setAccessible(flag);
            }
        }
    }
